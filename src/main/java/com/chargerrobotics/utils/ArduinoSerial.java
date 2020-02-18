package com.chargerrobotics.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortIOException;
import com.fazecast.jSerialComm.SerialPortTimeoutException;

public class ArduinoSerial {

	private final SerialPort serial;
	private boolean isOpen;
	private InputStream in;
	private OutputStream out;

	private static final long TIMEOUT = 5;
	private static final long RESPONSE_TIMEOUT = 20;

	private static final byte[] sync = new byte[] { 0x55, 0x55 };
	private static final byte[] poll = new byte[] { (byte) 0x81, 0x42 };
	
	private final String name;

	private static final Checksum cs = new Checksum();
	private final ByteBuffer dataBuffer = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);
	private int openCounter;
	private ArduinoListener listener = null;

	protected ArduinoSerial(String port) {
		this.name = port;
		serial = SerialPort.getCommPort(port);
		serial.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		open();
	}
	
	public String getName() {
		return name;
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
		} catch (SerialPortTimeoutException e) {
		} catch (SerialPortIOException e) {
			isOpen = false;
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

	public void sendData(byte header1, byte header2, byte[] data) {
		sendData(header1, header2, data, false);
	}

	public ByteBuffer sendData(byte header1, byte header2, byte[] data, boolean receive) {
		if (isOpen) {
			dataBuffer.position(0);
			dataBuffer.limit(data.length);
			dataBuffer.put(data);
			sendData(new byte[] {header1, header2}, data.length);
			if (receive) {
				int len = receiveData();
				if (len >= 0) {
					dataBuffer.limit(len);
					return dataBuffer;
				}
			}
		}
		return null;
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
				listener.setLastReceived();
				listener.receiveData(this, dataBuffer);
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
			byte[] receivedHeader = new byte[2];
			ArduinoListener listener = null;
			while (true) {
				try {
					if (in.available() > 0) {
						byte b = (byte) in.read();
						if (!hasSync) {
							if (b != 0x55)
								hasSync = true;
						}
						if (hasSync) {
							recCount++;
							if (!hasHeader) {
								if (recCount <= 2) {
									receivedHeader[recCount - 1] = b;
								} else if (recCount == 3) {
									listener = ArduinoSerialReceiver.getListener(receivedHeader);
									if (listener == null)
										return -1;
									else
										this.listener = listener;
									expectedChecksum = b & 0xff;
								} else if (recCount == 4) {
									expectedChecksum = expectedChecksum | ((b << 8) & 0xff);
								} else if (recCount == 5) {
									expectedLength = b & 0xff;
								} else if (recCount == 6) {
									expectedLength = expectedLength | ((b << 8) & 0xff);
									recCount = 0;
									hasHeader = true;
									if (recCount >= expectedLength) {
										dataBuffer.limit(0);
										dataBuffer.position(0);
										return 0;
									}
								}
							} else {
								dataBuffer.array()[recCount - 1] = b;
								cs.updateChecksum(b);
								if (recCount >= expectedLength) {
									if (cs.getChecksum() == expectedChecksum) {
										dataBuffer.limit(expectedLength);
										dataBuffer.position(0);
										return expectedLength;
									}
									return -1;
								}
							}
						}
					}
				} catch (SerialPortTimeoutException e) {
				} catch (SerialPortIOException e) {
					isOpen = false;
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
				int b = in.read();
				if (b == 0x55) {
					return true;
				}
			} catch (SerialPortTimeoutException e) {
			} catch (SerialPortIOException e) {
				isOpen = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (RESPONSE_TIMEOUT > 0 && System.currentTimeMillis() - start >= RESPONSE_TIMEOUT)
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
			out.write(dataBuffer.array(), 0, length);
		} catch (SerialPortTimeoutException e) {
		} catch (SerialPortIOException e) {
			isOpen = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

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
