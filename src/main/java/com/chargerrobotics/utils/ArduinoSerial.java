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

/**
 * Serial sender/receiver to communicate with an Arduino
 * Uses a 2 byte header to specify device type
 * Sends packets of up to 256 bytes, little endian
 * 
 * Serial Settings: 115200 baud, 8 data bits, 1 stop bit, no parity
 */
public class ArduinoSerial {

	private final SerialPort serial;
	private boolean isOpen;
	private boolean closed;
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

	/**
	 * Construcst a new ArduinoSerial on the given system COM port name
	 * 
	 * @param port System COM port name
	 * Ex: ttyUSB0
	 */
	protected ArduinoSerial(String port) {
		this.name = port;
		serial = SerialPort.getCommPort(port);
		serial.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		open();
	}

	/**
	 * Construcst a new ArduinoSerial on the given COM port
	 * 
	 * @param port System COM port name
	 * Ex: ttyUSB0
	 */
	protected ArduinoSerial(SerialPort serial) {
		this.serial = serial;
		this.name = serial.getSystemPortName();
		serial.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		open();
	}
	
	/**
	 * Returns the COM port name in use
	 * 
	 * @return System COM port name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Internal method to open the port
	 */
	private void open() {
		isOpen = serial.openPort();
		if (isOpen) {
			in = serial.getInputStream();
			out = serial.getOutputStream();
		}
	}

	/**
	 * Closes the serial port
	 */
	public void close() {
		isOpen = false;
		closed = true;
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
		serial.closePort();
	}

	/**
	 * Sends the given byte array of data to the device with the specified header
	 * 
	 * Does not wait for a response from the device
	 * 
	 * @param header Header to be sent
	 * @param data Data to be sent
	 * 
	 * @return A read-only {@link java.nio.ByteBuffer} containing the received data
	 */
	public void sendData(short header, byte[] data) {
		sendData(header, data, false);
	}

	/**
	 * Sends the given byte array of data to the device with the specified header
	 * 
	 * <p>Blocks for a short period of time until the request times out if receiving data
	 * 
	 * @param header Header to be sent
	 * @param data Data to be sent
	 * @param receive Whether to wait for a response from the device
	 * 
	 * @return A read-only {@link java.nio.ByteBuffer} containing the received data, if available
	 */
	public ByteBuffer sendData(short header, byte[] data, boolean receive) {
		if (closed)
			return null;
		if (!isOpen) {
			open();
			openCounter = 0;
		}
		if (isOpen) {
			sendBuffer.position(0);
			if (data != null) {
				sendBuffer.limit(data.length);
				sendBuffer.put(data);
			} else
				sendBuffer.limit(0);
			sendData(new byte[] {(byte)(header & 0xff), (byte)((header >> 8) & 0xff)}, sendBuffer.limit());
			if (receive) {
				Pair<ArduinoListener, Integer> rec = receiveData(false);
				if (rec.getValue() >= 0) {
					return recBuffer.asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN);
				}
			}
		}
		return null;
	}

	/**
	 * Sends the polling message header to poll the connected device for data
	 * 
	 * If the port is not currently open, it will increment a counter up to 50 before attempting to open the port again and will return <code>false</code> for each ignored attempt to avoid unnecessary blocking
	 * 
	 * <p>Blocks for a short period of time until the request times out
	 * 
	 * @return <code>true</code> if the polling was successful
	 */
	public boolean poll() {
		if (closed)
			return false;
		if (!isOpen) {
			openCounter++;
			if (openCounter >= 50) {
				open();
				openCounter = 0;
			}
		}
		if (isOpen) {
			sendData(poll, 0);
			Pair<ArduinoListener, Integer> rec = receiveData(true);
			if (rec != null && rec.getValue() >= 0) {
				listener = rec.getKey();
				listener.setLastReceived();
				listener.savePortID(getName());
				listener.receiveData(this, recBuffer.asReadOnlyBuffer().order(ByteOrder.LITTLE_ENDIAN));
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the {@link ArduinoListener} that has been found for this serial port, or <code>null</code> if unavailable
	 * 
	 * @return {@link ArduinoListener} for this port
	 */
	public ArduinoListener getListener() {
		return listener;
	}

	/**
	 * Internal method for receiving data into the receive {@link java.nio.ByteBuffer}
	 * 
	 * Message checksum and length are automatically validated to confirm that the packet was received successfully
	 * 
	 * <p>Blocks for a short period of time until the request times out
	 * 
	 * <p>Receives remaining sync bytes to ensure proper serial operation before reading the message
	 * <p>Receives 2 byte header to identify a registered listener
	 * <p>Receives message checksum
	 * <p>Receives length of message to be received
	 * <p>Receives message and stores in the receive {@link java.nio.ByteBuffer}
	 * 
	 * @param poll Whether to expect a response to a poll
	 * 
	 * @return A {@link Pair} containing the listener to be called and the number of bytes received or null if the packet was invalid
	 */
	private synchronized Pair<ArduinoListener, Integer> receiveData(boolean poll) {
		if (findSync()) {
			byte state = 0;
			long start = System.currentTimeMillis();
			int bytesRead = 0;
			recCs.reset();
			int expectedChecksum = 0;
			int expectedLength = 0;
			short currentHeader = 0;
			ArduinoListener listener = null;
			while (true) {
				try {
					if (in.available() > 0) {
						byte b = (byte) in.read();
						switch (state) {
						case 0:
							if (b == sync[0]) {
								state++;
								break;
							}
						case 1:
							currentHeader = (short)(b & 0xff);
							state++;
							break;
						case 2:
							currentHeader |= (short)((b & 0xff) << 8);
							state++;
							if (poll) {
								listener = ArduinoSerialReceiver.getListener(currentHeader);
								if (listener == null)
									return null;
							}
							break;
						case 3:
							expectedChecksum = b & 0xff;
							state++;
							break;
						case 4:
							expectedChecksum |= ((b << 8) & 0xff);
							state++;
							break;
						case 5:
							expectedLength = b & 0xff;
							state++;
							break;
						case 6:
							expectedLength |= ((b << 8) & 0xff);
							state++;
							bytesRead = 0;
							if (expectedLength == 0) {
								recBuffer.limit(0);
								recBuffer.position(0);
								return new Pair<ArduinoListener, Integer>(listener, 0);
							}
							break;
						case 7:
							recBuffer.array()[bytesRead] = b;
							bytesRead++;
							recCs.updateChecksum(b);
							if (bytesRead >= expectedLength) {
								if (recCs.getChecksum() == expectedChecksum) {
									recBuffer.limit(expectedLength);
									recBuffer.position(0);
									return new Pair<ArduinoListener, Integer>(listener, expectedLength);
								}
								return null;
							}
						}
					}
				} catch (SerialPortTimeoutException e) {
				} catch (SerialPortIOException e) {
					isOpen = false;
					return null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (TIMEOUT > 0 && System.currentTimeMillis() - start >= TIMEOUT)
					return null;
			}
		}
		return null;
	}

	/**
	 * Internal method for finding the initial sync bytes to ensure proper serial operation before reading
	 * 
	 * <p>Blocks for a short period of time until the request times out
	 * 
	 * @return <code>true</code> if a sync byte was found
	 */
	private synchronized boolean findSync() {
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

	/**
	 * Internal method for sending the current contents of the send {@link java.nio.ByteBuffer}
	 * 
	 * <p>Sends sync bytes to ensure proper serial operation before reading the message
	 * <p>Sends 2 byte header to indicate message contents
	 * <p>Sends message checksum
	 * <p>Sends length of message to be sent
	 * <p>Sends message stored in the send {@link java.nio.ByteBuffer}
	 * 
	 * @param msgType 2 byte header
	 * @param length Number of bytes from buffer to send
	 */
	private synchronized void sendData(byte[] msgType, int length) {
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

	/**
	 * Returns whether or not the serial port is open
	 * 
	 * @return Serial port status
	 */
	public boolean isOpen() {
		return closed ? false : isOpen;
	}
	
	/**
	 * Returns the COM port name in use
	 * 
	 * Equivalent to {@link #getName()}
	 * 
	 * @return System COM port name
	 */
	public String toString() {
		return getName();
	}
	
	/**
	 * Pair holder class
	 *
	 * @param <K> Key
	 * @param <V> Value
	 */
	public static class Pair<K, V> {
		private final K key;
		private final V value;
		/**
		 * Constructs a new pair with the given key and value
		 * 
		 * @param key Key
		 * @param value Value
		 */
		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Returns the stored key
		 * 
		 * @return Key
		 */
		public K getKey() {
			return this.key;
		}
		
		/**
		 * Returns the stored value
		 * 
		 * @return Value
		 */
		public V getValue() {
			return this.value;
		}
	}

	/**
	 * Checksum holder class
	 */
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
