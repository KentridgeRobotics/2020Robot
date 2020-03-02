package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@SuppressWarnings("unused")
public class LEDSubsystem extends SubsystemBase {
	private static LEDSubsystem instance;

	private static final int LED_COUNT = 16;
	private static final int INTERVAL = 5;
	private static final int WAIT_INTERVAL = 10;

	private LEDMode mode;
	private AddressableLED leds;
	private AddressableLEDBuffer buffer;

	private int counter = -WAIT_INTERVAL;

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
	}

	public void setMode(LEDMode mode) {
		this.mode = mode;
		switch (mode) {
		case DISABLED:
			setAllRGB(0, 0, 0);
			break;
		case TELEOP:
			setAllHSV(100, 255, 15);
			break;
		case AUTONOMOUS:
			counter = -WAIT_INTERVAL;
			buffer.setRGB(0, 255, 0, 0);
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
			counter++;
			if (counter % INTERVAL == 0 && counter > 0) {
				if (counter < INTERVAL * LED_COUNT) {
					if ((counter / INTERVAL) - 2 >= 0)
						buffer.setRGB((counter / INTERVAL) - 2, 0, 0, 0);
					buffer.setRGB((counter / INTERVAL) - 1, 40, 0, 0);
					buffer.setRGB(counter / INTERVAL, 255, 0, 0);
				} else if (counter == INTERVAL * LED_COUNT) {
					buffer.setRGB(LED_COUNT - 2, 0, 0, 0);
				} else if (counter >= (LED_COUNT * INTERVAL * 2) + INTERVAL + WAIT_INTERVAL) {
					counter = -WAIT_INTERVAL;
				} else if (counter >= (LED_COUNT * INTERVAL * 2) + WAIT_INTERVAL) {
					buffer.setRGB(1, 0, 0, 0);
				} else if (counter > (INTERVAL * LED_COUNT) + WAIT_INTERVAL) {
					if ((LED_COUNT * 2 - 1) - ((counter - WAIT_INTERVAL) / INTERVAL) + 2 < LED_COUNT)
						buffer.setRGB((LED_COUNT * 2 - 1) - ((counter - WAIT_INTERVAL) / INTERVAL) + 2, 0, 0, 0);
					buffer.setRGB((LED_COUNT * 2 - 1) - ((counter - WAIT_INTERVAL) / INTERVAL) + 1, 40, 0, 0);
					buffer.setRGB((LED_COUNT * 2 - 1) - ((counter - WAIT_INTERVAL) / INTERVAL), 255, 0, 0);
				}
			}
			leds.setData(buffer);
			return;
		}
	}

	public static enum LEDMode {
		DISABLED, TELEOP, AUTONOMOUS;
	}

}