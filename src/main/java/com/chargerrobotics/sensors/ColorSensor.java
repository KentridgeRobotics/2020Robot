package com.chargerrobotics.sensors;

import java.nio.ByteBuffer;

import com.chargerrobotics.Robot.ColorWheelColor;
import com.chargerrobotics.utils.ArduinoI2C;

public class ColorSensor extends ArduinoI2C {
	
	private ColorWheelColor current = null;

	public ColorSensor(int address) {
		super(address);
	}
	
	protected void recData(ByteBuffer buffer) {
		current = ColorWheelColor.valueOf(buffer.getChar());
	}
	
	public ColorWheelColor getColor() {
		if (poll() == 0)
			return current;
		return null;
	}
	
}
