package com.chargerrobotics.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.fazecast.jSerialComm.SerialPort;

public abstract class ArduinoSerial {

	private final SerialPort serial;
	private boolean isOpen;
	private InputStream in;
	private OutputStream out;

	private static final long TIMEOUT = 2000;
	
	private static final byte[] sync = new byte[] { 85, 85 };
	private static final byte[] poll = new byte[] { -127, 66 };
	private static final byte[] response = new byte[] { 45, -120 };
	
	private static final Checksum cs = new Checksum();
	private final ByteBuffer dataBuffer = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);
	private int openCounter;

	protected ArduinoSerial(String port) {
		serial = SerialPort.getCommPort(port);
		serial.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		open();
	}

	private void open() {
		isOpen = serial.openPort();
		if (isOpen) {
			in = serial.getInputStream();
			out = serial.getOutputStream();
		}
	}

	public void close() {
		isOpen = false;
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void calculateChecksum() {
		cs.reset();
		for (byte b : dataBuffer.array()) {
			cs.updateChecksum(b);
		}
	}

	public boolean poll() {
		if (!isOpen) {
			openCounter++;
			if (openCounter >= 50) {
				open();
				openCounter = 0;
			}
		}
		if (isOpen) {
			sendData(poll, 0);
			int len = receiveData();
			if (len >= 0) {
				dataBuffer.limit(len);
				recData(dataBuffer);
				return true;
			}
		}
		return false;
	}

	private int receiveData() {
		if (findSync()) {
			long start = System.currentTimeMillis();
			boolean hasSync = false;
			boolean hasHeader = false;
			int recCount = 0;
			cs.reset();
			int expectedChecksum = 0;
			int expectedLength = 0;
			while (true) {
				try {
					if (in.available() > 0) {
						byte b = (byte) in.read();
						if (!hasSync) {
							if (b != 85)
								hasSync = true;
						} else {
							recCount++;
							if (!hasHeader) {
								if (recCount <= 2) {
									if (b != response[recCount - 1])
										return -1;
								} else if (recCount == 3) {
									expectedChecksum = b & 0xff;
								} else if (recCount == 4) {
									expectedChecksum = expectedChecksum | ((b << 8) & 0xff);
								} else if (recCount == 5) {
									expectedLength = b & 0xff;
								} else if (recCount == 6) {
									expectedLength = expectedLength | ((b << 8) & 0xff);
									recCount = 0;
								}
							} else {
								dataBuffer.array()[recCount - 1] = b;
								cs.updateChecksum(b);
								if (recCount >= expectedLength) {
									if (cs.getChecksum() == expectedChecksum) {
										dataBuffer.limit(expectedLength);
										return expectedLength;
									}
									return -1;
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (TIMEOUT > 0 && System.currentTimeMillis() - start >= TIMEOUT)
					return -1;
			}
		}
		return -1;
	}

	private boolean findSync() {
		long start = System.currentTimeMillis();
		while (true) {
			try {
				if (in.read() == 85)
					return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (TIMEOUT > 0 && System.currentTimeMillis() - start >= TIMEOUT)
				return false;
		}
	}

	private void sendData(byte[] msgType, int length) {
		try {
			calculateChecksum();
			out.write(sync);
			out.write(msgType);
			out.write(cs.getArray());
			out.write(new byte[] { (byte) (length & 0xff), (byte) ((length >> 8) & 0xff) });
			out.write(dataBuffer.array());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	protected abstract void recData(ByteBuffer buffer);

	// Checksum holder class
	public static class Checksum {

		int cs = 0;

		/**
		 * Adds byte to checksum
		 * 
		 * @param b Byte to be added
		 */
		public void updateChecksum(int b) {
			cs += b;
		}

		/**
		 * Returns calculated checksum
		 * 
		 * @return Calculated checksum
		 */
		public int getChecksum() {
			return cs;
		}

		/**
		 * Resets checksum
		 */
		public void reset() {
			cs = 0;
		}

		public byte[] getArray() {
			return new byte[] { (byte) (cs & 0xff), (byte) ((cs >> 8) & 0xff) };
		}
	}
}
