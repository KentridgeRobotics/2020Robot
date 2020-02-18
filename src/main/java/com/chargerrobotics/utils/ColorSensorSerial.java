package com.chargerrobotics.utils;

import java.nio.ByteBuffer;

import com.chargerrobotics.Robot.ColorWheelColor;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class ColorSensorSerial extends ArduinoListener {
	
	public ColorSensorSerial() {
		ArduinoSerialReceiver.registerListener(this, (byte) 0x2D, (byte) 0x86);
	}

	private ColorWheelColor color = null;

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		color = ColorWheelColor.valueOf((char) buffer.get());
	}
	
	public ColorWheelColor getColor() {
		return isExpired() ? null : color;
	}

}
