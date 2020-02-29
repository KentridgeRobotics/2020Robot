package com.chargerrobotics.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.fazecast.jSerialComm.SerialPort;

/**
 * Controller to handle all {@link ArduinoSerial} objects and listeners
 */
public class ArduinoSerialReceiver {

	private static final long POLL_INTERVAL = 40;
	
	private static final ConcurrentHashMap<Short, ArduinoListener> responseHeaders = new ConcurrentHashMap<Short, ArduinoListener>();
	private static final List<ArduinoSerial> serialPorts = Collections.synchronizedList(new ArrayList<ArduinoSerial>());
	
	private static Thread startThread = null;
	private static Timer pollTimer = new Timer();
	private static volatile PollTask pollTask = null;

	/**
	 * Looks for all available USB to serial COM ports and runs the provided initialization commands before closing them
	 * 
	 * Useful for resetting Arduinos
	 * 
	 * @param initCommands Commands to run at initialization
	 */
	public static void initialization(Runnable initCommands) {
		synchronized(serialPorts) {
			SerialPort[] availablePorts = SerialPort.getCommPorts();
			if (availablePorts == null) {
				return;
			}
			for (SerialPort availablePort : availablePorts) {
				if (availablePort.toString().contains("USB-to-Serial")) {
					serialPorts.add(new ArduinoSerial(availablePort));
				}
			}
			initCommands.run();
			serialPorts.removeIf(serial -> {serial.close(); return true;});
		}
	}
	
	/**
	 * Looks for all available USB to serial COM ports and begins polling them for data
	 */
	public static void start() {
		startThread = new Thread(() -> {
			if (pollTask != null) {
				pollTask.stop();
			}
			pollTask = new PollTask();
			synchronized(serialPorts) {
				for (SerialPort availablePort : SerialPort.getCommPorts()) {
					if (availablePort.toString().contains("USB-to-Serial")) {
						serialPorts.add(new ArduinoSerial(availablePort));
					}
				}
			}
			pollTimer.scheduleAtFixedRate(pollTask, 0, POLL_INTERVAL);
		});
		startThread.start();
	}
	
	/**
	 * Gets the {@link ArduinoSerial} port with the given name
	 * 
	 * @param name Port name
	 */
	private static ArduinoSerial getSerialPort(String name) {
		synchronized(serialPorts) {
			for (ArduinoSerial serial : serialPorts) {
				if (serial.getName().equals(name)) {
					return serial;
				}
			}
		}
		return null;
	}
	
	/**
	 * Stops polling for data and disposes of currently open COM ports
	 */
	public static void close() {
		if (pollTask != null)
			pollTask.stop();
	}

	/**
	 * Registers a listener for data with the given header
	 * 
	 * @param listener Listener
	 * @param header Header to listen for
	 */
	public static void registerListener(ArduinoListener listener, short header) {
		responseHeaders.put(header, listener);
	}

	/**
	 * Internal method to get the listener for the given header
	 * 
	 * @param header Received header
	 * 
	 * @return Listener
	 */
	protected static ArduinoListener getListener(short header) {
		return responseHeaders.get(header);
	}
	
	private static class PollTask extends TimerTask {

		/**
		 * Polls all currently open COM ports
		 */
		@Override
		public void run() {
			synchronized(serialPorts) {
				for (ArduinoSerial port : serialPorts) {
					port.poll();
				}
			}
		}
		
		/**
		 * Schedules the timer to stop and dispose of currently open COM ports
		 */
		public void stop() {
			this.cancel();
			synchronized(serialPorts) {
				serialPorts.removeIf(serial -> {serial.close(); return true;});
			}
		}
		
	}

	/**
	 * Listener class for received messages
	 */
	public static abstract class ArduinoListener {
		private volatile long lastReceived = 0;
		private final long expiry;
		private String id = "";
		
		/**
		 * Constructs a listener with the default message expiry time of <code>50ms</code>
		 */
		public ArduinoListener() {
			this(50);
		}
		
		/**
		 * Constructs a listener with the given message expiry time
		 */
		public ArduinoListener(long expiry) {
			this.expiry = expiry;
		}

		/**
		 * Internal method to be run when data has been received
		 * 
		 * @param serial Serial object that the data was received by
		 * @param data Read-only {@link java.nio.ByteBuffer} containing the received data
		 */
		public abstract void receiveData(ArduinoSerial serial, ByteBuffer data);

		/**
		 * Internal method for setting the time the last message was received at
		 */
		void setLastReceived() {
			lastReceived = System.currentTimeMillis();
		}

		/**
		 * Internal method for saving the port ID to config
		 * 
		 * @param id Port id
		 */
		void savePortID(String id) {
			this.id = id;
			COMPortsStorage.savePort(this.getClass().getSimpleName(), id);
		}

		/**
		 * Gets the {@link ArduinoSerial} object tied to this listener if available
		 * 
		 * @return {@link ArduinoSerial} last associated with this listener, or <code>null</code> if unavailable
		 */
		public ArduinoSerial getSerialPort() {
			if (id == null || id.length() == 0)
				id = COMPortsStorage.getPort(this.getClass().getSimpleName());
			if (id != null && id.length() > 0) {
				return ArduinoSerialReceiver.getSerialPort(id);
			}
			return null;
		}

		/**
		 * Returns the time the last message was received at
		 * 
		 * @return UNIX timestamp of last message received in milliseconds
		 */
		public long getLastMessageTime() {
			return lastReceived;
		}

		/**
		 * Returns whether or not the last data received has expired
		 * 
		 * @return <code>true</code> if the last received data has expired
		 */
		public boolean isExpired() {
			return expiry < 0 || System.currentTimeMillis() - lastReceived > expiry;
		}

		/**
		 * Returns the set expiry time
		 * 
		 * @return Expiry time
		 */
		public long getExpiryTime() {
			return expiry;
		}
	}

}
