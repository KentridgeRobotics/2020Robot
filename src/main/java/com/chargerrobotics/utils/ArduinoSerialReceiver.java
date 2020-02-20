package com.chargerrobotics.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoSerialReceiver {

	private static final ConcurrentHashMap<Short, ArduinoListener> responseHeaders = new ConcurrentHashMap<Short, ArduinoListener>();
	private static final List<ArduinoSerial> serialPorts = Collections.synchronizedList(new ArrayList<ArduinoSerial>());
	
	private static Thread startThread = null;
	private static Timer pollTimer = new Timer();
	private static volatile PollTask pollTask = null;
	
	public static void start() {
		startThread = new Thread(() -> {
			pollTask = new PollTask();
			synchronized(serialPorts) {
				for (SerialPort availablePort : SerialPort.getCommPorts()) {
					if (availablePort.toString().contains("USB-to-Serial")) {
						String name = availablePort.getSystemPortName();
						serialPorts.add(new ArduinoSerial(name));
					}
				}
			}
			pollTimer.scheduleAtFixedRate(pollTask, 0, 20);
		});
		startThread.start();
	}
	
	public static void close() {
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
				for (ArduinoSerial port : serialPorts) {
					port.poll();
				}
				if (stop) {
					Iterator<ArduinoSerial> iter = serialPorts.iterator();
					while (iter.hasNext()) {
						ArduinoSerial serial = iter.next();
						serial.close();
						serialPorts.remove(serial);
					}
					this.cancel();
				}
			}
		}
		
		public void stop() {
			stop = true;
		}
		
	}

	public static abstract class ArduinoListener {
		private long lastReceived = 0;
		private final long expiry;
		
		public ArduinoListener() {
			this(50);
		}
		
		public ArduinoListener(long expiry) {
			this.expiry = expiry;
		}

		public abstract void receiveData(ArduinoSerial serial, ByteBuffer data);

		void setLastReceived() {
			lastReceived = System.currentTimeMillis();
		}

		public long getLastMessageTime() {
			return lastReceived;
		}

		public boolean isExpired() {
			return expiry < 0 || System.currentTimeMillis() - lastReceived > expiry;
		}

		public long getExpiryTime() {
			return expiry;
		}
	}

}
