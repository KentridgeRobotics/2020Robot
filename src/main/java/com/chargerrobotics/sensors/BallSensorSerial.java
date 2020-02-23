package com.chargerrobotics.sensors;

import java.nio.ByteBuffer;

import com.chargerrobotics.utils.ArduinoSerial;
import com.chargerrobotics.utils.ArduinoSerialReceiver;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class BallSensorSerial extends ArduinoListener {
	
	public BallSensorSerial() {
		ArduinoSerialReceiver.registerListener(this, (short)0xB02D);
	}

	private byte ballCount = 0;

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		if (buffer.hasRemaining())
			ballCount = buffer.get();
		System.out.println(getBallCount());
	}
	
	public void resetCount() {
		ArduinoSerial port = getSerialPort();
		if (port != null) {
			port.sendData((short)0xB0FF, null);
		}
	}
	
	public byte getBallCount() {
		return isExpired() ? -1 : ballCount;
	}

}
