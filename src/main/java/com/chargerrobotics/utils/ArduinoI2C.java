package com.chargerrobotics.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.wpi.first.wpilibj.I2C;

public abstract class ArduinoI2C {
	
	private final I2C i2c;

	private static final byte[] lengthRegister = new byte[] {0x01};
	private static final byte[] dataRegister = new byte[] {0x02};
	private final byte[] lengthBuffer = new byte[8];
	private final ByteBuffer dataBuffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);
	
	private int msgLength = 0;
	
	protected ArduinoI2C(int address) {
		this(address, I2C.Port.kOnboard);
	}
	
	protected ArduinoI2C(int address, I2C.Port port) {
		i2c = new I2C(port, address);
	}
	
	public int poll() {
		if (i2c.transaction(lengthRegister, 1, lengthBuffer, 1)) {
			msgLength = (((lengthBuffer[4] & 0xff) << 24) | ((lengthBuffer[2] & 0xff) << 16) | ((lengthBuffer[1] & 0xff) << 8) | (lengthBuffer[0] & 0xff));
			if (i2c.transaction(dataRegister, 1, dataBuffer.array(), msgLength)) {
				dataBuffer.limit(msgLength);
				byte status = dataBuffer.get();
				if (status >= 0)
					recData(dataBuffer);
				return status;
			}
		}
		return -1;
	}
	
	protected abstract void recData(ByteBuffer buffer);
}
