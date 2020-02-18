package com.chargerrobotics.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoSerialReceiver {

	private static final HashMap<byte[], ArduinoListener> responseHeaders = new HashMap<byte[], ArduinoListener>();
	private static final ArrayList<ArduinoSerial> serialPorts = new ArrayList<ArduinoSerial>();

	public static void init() {
		for (SerialPort availablePort : SerialPort.getCommPorts()) {
			if (availablePort.toString().contains("USB-to-Serial")) {
				serialPorts.add(new ArduinoSerial(availablePort.getSystemPortName()));
			}
		}
	}

	public static void poll() {
		for (ArduinoSerial port : serialPorts) {
			port.poll();
		}
	}

	public static void registerListener(ArduinoListener listener, byte header1, byte header2) {
		responseHeaders.put(new byte[] { header1, header2 }, listener);
	}

	protected static ArduinoListener getListener(byte[] header) {
		for (Entry<byte[], ArduinoListener> entry : responseHeaders.entrySet()) {
			byte[] test = entry.getKey();
			if (test[0] == header[0] && test[1] == header[1])
				return entry.getValue();
		}
		return null;
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
