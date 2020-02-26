package com.chargerrobotics.sensors;

import java.nio.ByteBuffer;

import com.chargerrobotics.utils.ArduinoSerial;
import com.chargerrobotics.utils.ArduinoSerialReceiver;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class ScaleSerial extends ArduinoListener {

	private volatile float reading = 0;
	
	public ScaleSerial() {
		ArduinoSerialReceiver.registerListener(this, (short)0x902D);
	}

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		if (buffer.remaining() >= 4)
			reading = buffer.getFloat();
	}
	
	/**
	 * Get the current scale reading or {@code -1} if data is expired
	 * 
	 * @return Scale reading
	 */
	public float getReading() {
		return isExpired() ? -1 : reading;
	}

}
