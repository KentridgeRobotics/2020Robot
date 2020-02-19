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

	private static final Checksum recCs = new Checksum();
	private static final Checksum sendCs = new Checksum();
	private final ByteBuffer recBuffer = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);
	private final ByteBuffer sendBuffer = ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN);
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
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendData(short header, byte[] data) {
		sendData(header, data, false);
	}

	public ByteBuffer sendData(short header, byte[] data, boolean receive) {
		if (!isOpen) {
			open();
			openCounter = 0;
		}
		if (isOpen) {
			sendBuffer.position(0);
			sendBuffer.limit(data.length);
			sendBuffer.put(data);
			sendData(new byte[] {(byte)(header & 0xff), (byte)((header >> 8) & 0xff)}, data.length);
			if (receive) {
				int len = receiveData();
				if (len >= 0) {
					recBuffer.limit(len);
					return recBuffer;
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
				recBuffer.limit(len);
				listener.setLastReceived();
				listener.receiveData(this, recBuffer.asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN));
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
			recCs.reset();
			int expectedChecksum = 0;
			int expectedLength = 0;
			short recHeader = 0;
			ArduinoListener listener = null;
			while (true) {
				try {
					if (in.available() > 0) {
						byte b = (byte) in.read();
						if (!hasSync) {
							if (b != sync[0])
								hasSync = true;
						}
						if (hasSync) {
							recCount++;
							if (!hasHeader) {
								if (recCount <= 1) {
									recHeader = (short)(b & 0xff);
								} else if (recCount <= 2) {
									recHeader |= (short)((b & 0xff) << 8);
									listener = ArduinoSerialReceiver.getListener(recHeader);
									if (listener == null)
										return -1;
									else
										this.listener = listener;
								} else if (recCount == 3) {
									expectedChecksum = b & 0xff;
								} else if (recCount == 4) {
									expectedChecksum = expectedChecksum | ((b << 8) & 0xff);
								} else if (recCount == 5) {
									expectedLength = b & 0xff;
								} else if (recCount == 6) {
									expectedLength = expectedLength | ((b << 8) & 0xff);
									recCount = 0;
									hasHeader = true;
									if (expectedLength == 0) {
										recBuffer.limit(0);
										recBuffer.position(0);
										return 0;
									}
								}
							} else {
								recBuffer.array()[recCount - 1] = b;
								recCs.updateChecksum(b);
								if (recCount >= expectedLength) {
									if (recCs.getChecksum() == expectedChecksum) {
										recBuffer.limit(expectedLength);
										recBuffer.position(0);
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
				if (b == sync[0]) {
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
			sendCs.reset();
			byte[] sendArr = sendBuffer.array();
			for (int i = 0; i < length; i++) {
				sendCs.updateChecksum(sendArr[i]);
			}
			out.write(sync);
			out.write(msgType);
			out.write(sendCs.getArray());
			out.write(new byte[] { (byte) (length & 0xff), (byte) ((length >> 8) & 0xff) });
			out.write(sendBuffer.array(), 0, length);
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
	
	public String toString() {
		return getName();
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
