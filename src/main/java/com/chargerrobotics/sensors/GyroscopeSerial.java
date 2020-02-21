package com.chargerrobotics.sensors;

import java.nio.ByteBuffer;

import com.chargerrobotics.utils.ArduinoSerial;
import com.chargerrobotics.utils.ArduinoSerialReceiver;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class GyroscopeSerial extends ArduinoListener {
	
	public GyroscopeSerial() {
		ArduinoSerialReceiver.registerListener(this, (short)0xA02D);
	}

	private float x = 0;
	private float y = 0;
	private float z = 0;

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		if (buffer.remaining() >= 12) {
			x = buffer.getFloat();
			y = buffer.getFloat();
			z = buffer.getFloat();
		}
	}
	
	public float getX() {
		return isExpired() ? null : x;
	}
	
	public float getY() {
		return isExpired() ? null : y;
	}
	
	public float getZ() {
		return isExpired() ? null : z;
	}

}
