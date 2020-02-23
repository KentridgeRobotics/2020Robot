package com.chargerrobotics.sensors;

import java.nio.ByteBuffer;

import com.chargerrobotics.Robot.ColorWheelColor;
import com.chargerrobotics.utils.ArduinoSerial;
import com.chargerrobotics.utils.ArduinoSerialReceiver;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class ColorSensorSerial extends ArduinoListener {
	
	public ColorSensorSerial() {
		ArduinoSerialReceiver.registerListener(this, (short)0x802D);
	}

	private ColorWheelColor color = null;

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		if (buffer.hasRemaining())
			color = ColorWheelColor.valueOf((char) buffer.get());
	}
	
	public ColorWheelColor getColor() {
		return isExpired() ? null : color;
	}

}
