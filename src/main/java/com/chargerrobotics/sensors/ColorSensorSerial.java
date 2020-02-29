package com.chargerrobotics.sensors;

import java.nio.ByteBuffer;

import com.chargerrobotics.Robot.ColorWheelColor;
import com.chargerrobotics.utils.ArduinoSerial;
import com.chargerrobotics.utils.ArduinoSerialReceiver;
import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class ColorSensorSerial extends ArduinoListener {

	private volatile ColorWheelColor color = null;
	
	public ColorSensorSerial() {
		ArduinoSerialReceiver.registerListener(this, (short)0x802D);
	}

	public void receiveData(ArduinoSerial serial, ByteBuffer buffer) {
		if (buffer.hasRemaining())
			color = ColorWheelColor.valueOf((char) buffer.get());
		System.out.println(isExpired() ? "Expired" : getColor());
	}
	
	/**
	 * Get the current wheel sensor color or {@code null} if data is expired
	 * 
	 * @return Color sensor color
	 */
	public ColorWheelColor getColor() {
		return isExpired() ? null : color;
	}

}
