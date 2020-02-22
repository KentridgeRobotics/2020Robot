package com.chargerrobotics.sensors;

import java.nio.ByteBuffer;

import com.chargerrobotics.utils.ArduinoSerial;
import com.chargerrobotics.utils.ArduinoSerialReceiver;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class ScaleSerial extends ArduinoListener {
	
	public ScaleSerial() {
		ArduinoSerialReceiver.registerListener(this, (short)0x902D);
	}

	private float reading = 0;

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		if (buffer.remaining() >= 4)
			reading = buffer.getFloat();
	}
	
	public float getReading() {
		return isExpired() ? null : reading;
	}

}
