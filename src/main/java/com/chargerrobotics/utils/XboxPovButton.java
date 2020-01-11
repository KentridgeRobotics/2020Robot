package com.chargerrobotics.utils;

import edu.wpi.first.wpilibj2.command.button.Button;

public class XboxPovButton extends Button {

	private final edu.wpi.first.wpilibj.XboxController controller;
	private final POVDirection povDirection;

	public XboxPovButton(edu.wpi.first.wpilibj.XboxController controller, POVDirection povDirection) {
		this.controller = controller;
		this.povDirection = povDirection;
	}

	/**
	 * Gets the value of the joystick button.
	 *
	 * @return The value of the joystick button
	 */
	@Override
	public boolean get() {
		switch (povDirection) {
		case UP:
			return (controller.getPOV(0) == 315 || controller.getPOV(0) == 0 || controller.getPOV(0) == 45) ? true
					: false;
		case RIGHT:
			return (controller.getPOV(0) == 45 || controller.getPOV(0) == 90 || controller.getPOV(0) == 135) ? true
					: false;
		case DOWN:
			return (controller.getPOV(0) == 135 || controller.getPOV(0) == 180 || controller.getPOV(0) == 225) ? true
					: false;
		case LEFT:
			return (controller.getPOV(0) == 225 || controller.getPOV(0) == 270 || controller.getPOV(0) == 315) ? true
					: false;
		default:
			return false;
		}
	}

	public enum POVDirection {
		UP, RIGHT, DOWN, LEFT;
	}

}