package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@SuppressWarnings("unused")
public class LEDSubsystem extends SubsystemBase {
	private static LEDSubsystem instance;

	private static final int LED_COUNT = 24;
	private static final int INTERVAL = 3;
	private static final int WAIT_INTERVAL = 10;
	private static final int AUTO_RED_VALUE = 255;
	private static final int AUTO_DIM_RED_VALUE = 40;

	private LEDMode mode;
	private AddressableLED leds;
	private AddressableLEDBuffer buffer;
	private LED_STATE led_state; 
	private int led_state_count; 

	private  int counter = WAIT_INTERVAL;

	public static LEDSubsystem getInstance() {
		if (instance == null) {
			instance = new LEDSubsystem();
			CommandScheduler.getInstance().registerSubsystem(instance);
		}
		return instance;
	}

	public LEDSubsystem() {
		leds = new AddressableLED(Constants.leds);
		buffer = new AddressableLEDBuffer(LED_COUNT);
		leds.setLength(LED_COUNT);
		setMode(LEDMode.DISABLED);
		leds.start();
		led_state = LED_STATE.LED_RIGHT; 
		led_state_count = 0; 
	}

	public void setMode(LEDMode mode) {
		this.mode = mode;
		switch (mode) {
		case DISABLED:
			setAllRGB(0, 0, 0);
			break;
		case TELEOP:
			if (DriverStation.getInstance().getAlliance() == Alliance.Blue)
				setAllHSV(100, 255, 15);
			else if (DriverStation.getInstance().getAlliance() == Alliance.Red)
				setAllHSV(0, 255, 15);
			else
				setAllHSV(55, 255, 15);
			break;
		case AUTONOMOUS:
			counter = -WAIT_INTERVAL;
			buffer.setRGB(0, AUTO_RED_VALUE, 0, 0);
			for (int i = 1; i < LED_COUNT; i++)
				buffer.setRGB(i, 0, 0, 0);
			break;
		}
		leds.setData(buffer);
	}

	  /**
	   * Sets all leds in the buffer.
	   *
	   * @param r     the r value [0-255]
	   * @param g     the g value [0-255]
	   * @param b     the b value [0-255]
	   */
	private void setAllRGB(int r, int g, int b) {
		for (int i = 0; i < LED_COUNT; i++)
			buffer.setRGB(i, r, g, b);
	}

	  /**
	   * Sets all leds in the buffer.
	   *
	   * @param h     the h value [0-180]
	   * @param s     the s value [0-255]
	   * @param v     the v value [0-255]
	   */
	private void setAllHSV(int h, int s, int v) {
		if (s == 0) {
			for (int i = 0; i < LED_COUNT; i++)
				buffer.setRGB(i, v, v, v);
			return;
		}

		final int region = h / 30;
		final int remainder = (h - (region * 30)) * 6;

		final int p = (v * (255 - s)) >> 8;
		final int q = (v * (255 - ((s * remainder) >> 8))) >> 8;
		final int t = (v * (255 - ((s * (255 - remainder)) >> 8))) >> 8;

		switch (region) {
		case 0:
			for (int i = 0; i < LED_COUNT; i++)
				buffer.setRGB(i, v, t, p);
			break;
		case 1:
			for (int i = 0; i < LED_COUNT; i++)
				buffer.setRGB(i, q, v, p);
			break;
		case 2:
			for (int i = 0; i < LED_COUNT; i++)
				buffer.setRGB(i, p, v, t);
			break;
		case 3:
			for (int i = 0; i < LED_COUNT; i++)
				buffer.setRGB(i, p, q, v);
			break;
		case 4:
			for (int i = 0; i < LED_COUNT; i++)
				buffer.setRGB(i, t, p, v);
			break;
		default:
			for (int i = 0; i < LED_COUNT; i++)
				buffer.setRGB(i, v, p, q);
			break;
		}
	}

	  /**
	   * Sets all LEDs in the buffer.
	   *
	   * @param color The color of the LED
	   */
	private void setAllLED(Color color) {
		int r = (int) (color.red * 255);
		int g = (int) (color.green * 255);
		int b = (int) (color.blue * 255);
		for (int i = 0; i < LED_COUNT; i++)
			buffer.setRGB(i, r, g, b);
	}

	  /**
	   * Sets all LEDs in the buffer.
	   *
	   * @param color The color of the LED
	   */
	private void setAllLED(Color8Bit color) {
		for (int i = 0; i < LED_COUNT; i++)
			buffer.setRGB(i, color.red, color.green, color.blue);
	}

	public LEDMode getMode() {
		return mode;
	}

	@Override
	public void periodic() {
		super.periodic();
		switch (mode) {
		case DISABLED:
			return;
		case TELEOP:
			return;
		case AUTONOMOUS:
			if (counter % INTERVAL == 0) {
				switch(led_state){
					case LED_RIGHT:
						onLedRight();
						break;
					case LED_LEFT:
						onLedLeft();
						break;
				}
			}
			counter = (counter + 1) % INTERVAL;
			leds.setData(buffer);
			return;
		}
	}

	private void clearBuffer(AddressableLEDBuffer buf){
		for (int i = 0; i < buf.getLength(); i++){
			buf.setRGB(i, 0, 0, 0);
		}
	}

	private void onLedRight(){
		clearBuffer(buffer);

		buffer.setRGB(led_state_count, AUTO_DIM_RED_VALUE, 0, 0);
		buffer.setRGB(led_state_count  + 1, AUTO_RED_VALUE, 0, 0);

		led_state_count++;
		if (led_state_count >= LED_COUNT - 1){
			led_state_count = 0; 
			led_state = LED_STATE.LED_LEFT; 
		}
	}

	private void onLedLeft(){
		clearBuffer(buffer); 

		buffer.setRGB(LED_COUNT - led_state_count -1, AUTO_DIM_RED_VALUE, 0, 0);
		buffer.setRGB(LED_COUNT - led_state_count -2, AUTO_RED_VALUE, 0, 0);

		led_state_count++;
		if (led_state_count >= LED_COUNT - 1){
			led_state_count = 0; 
			led_state = LED_STATE.LED_RIGHT; 
		}
	}

	private static enum LED_STATE{
		LED_LEFT, LED_RIGHT;
	}

	public static enum LEDMode {
		DISABLED, TELEOP, AUTONOMOUS;
	}

}