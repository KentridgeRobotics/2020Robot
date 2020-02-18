package com.chargerrobotics.utils;

import java.nio.ByteBuffer;

import com.chargerrobotics.Robot.ColorWheelColor;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class ColorSensorSerial extends ArduinoListener {
	
	public ColorSensorSerial() {
		ArduinoSerialReceiver.registerListener(this, (short)0x2D86);
	}

	private ColorWheelColor color = null;

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		if (buffer.limit() > 0)
			color = ColorWheelColor.valueOf((char) buffer.get());
	}
	
	public ColorWheelColor getColor() {
		return isExpired() ? null : color;
	}

}
