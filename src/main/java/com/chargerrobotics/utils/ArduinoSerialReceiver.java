package com.chargerrobotics.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoSerialReceiver {

	private static final long POLL_INTERVAL = 20;
	
	private static final ConcurrentHashMap<Short, ArduinoListener> responseHeaders = new ConcurrentHashMap<Short, ArduinoListener>();
	private static final List<ArduinoSerial> serialPorts = Collections.synchronizedList(new ArrayList<ArduinoSerial>());
	
	private static Thread startThread = null;
	private static Timer pollTimer = new Timer();
	private static volatile PollTask pollTask = null;
	
	public static void start() {
		startThread = new Thread(() -> {
			if (pollTask != null) {
				pollTask.stop();
			}
			pollTask = new PollTask();
			synchronized(serialPorts) {
				for (SerialPort availablePort : SerialPort.getCommPorts()) {
					if (availablePort.toString().contains("USB-to-Serial")) {
						String name = availablePort.getSystemPortName();
						serialPorts.add(new ArduinoSerial(name));
					}
				}
			}
			pollTimer.scheduleAtFixedRate(pollTask, 0, POLL_INTERVAL);
		});
		startThread.start();
	}
	
	public static void close() {
		if (pollTask != null)
			pollTask.stop();
	}

	public static void registerListener(ArduinoListener listener, short header) {
		responseHeaders.put(header, listener);
	}

	protected static ArduinoListener getListener(short header) {
		return responseHeaders.get(header);
	}
	
	private static class PollTask extends TimerTask {
		
		private volatile boolean stop = false;

		@Override
		public void run() {
			synchronized(serialPorts) {
				if (stop) {
					serialPorts.removeIf(serial -> {serial.close(); return true;});
					this.cancel();
				} else {
					for (ArduinoSerial port : serialPorts) {
						port.poll();
					}
				}
			}
		}
		
		public void stop() {
			stop = true;
		}
		
	}

	public static abstract class ArduinoListener {
		private long lastReceived = 0;
		private long expiry = 50;

		public abstract void receiveData(ArduinoSerial serial, ByteBuffer data);

		void setLastReceived() {
			lastReceived = System.currentTimeMillis();
		}

		void setLastReceived(long expiry) {
			lastReceived = System.currentTimeMillis();
			this.expiry = expiry;
		}

		public long getLastMessageTime() {
			return lastReceived;
		}

		public boolean isExpired() {
			return System.currentTimeMillis() - lastReceived > expiry;
		}

		public long getExpiryTime() {
			return expiry;
		}
	}

}
