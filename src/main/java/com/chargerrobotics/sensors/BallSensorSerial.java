package com.chargerrobotics.sensors;

import java.nio.ByteBuffer;

import com.chargerrobotics.utils.ArduinoSerial;
import com.chargerrobotics.utils.ArduinoSerialReceiver;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class BallSensorSerial extends ArduinoListener {

	private volatile byte ballCount = 0;
	
	public BallSensorSerial() {
		ArduinoSerialReceiver.registerListener(this, (short)0xB02D);
	}

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		if (buffer.hasRemaining())
			ballCount = buffer.get();
		System.out.println(getBallCount());
	}
	
	/**
	 * Reset the number of balls in the ball sensor
	 */
	public void resetCount() {
		ArduinoSerial port = getSerialPort();
		if (port != null) {
			port.sendData((short)0xB0FF, null);
		}
	}
	
	/**
	 * Get the current ball sensor count or {@code -1} if data is expired
	 * 
	 * @return Ball sensor count
	 */
	public byte getBallCount() {
		return isExpired() ? -1 : ballCount;
	}

}
