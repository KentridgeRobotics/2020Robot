package org.usfirst.frc.team3786.robot;

import org.usfirst.frc.team3786.robot.utils.XboxController;

public class OI {

	private static XboxController primaryController = null;
	private static XboxController secondaryController = null;

	public static XboxController getPrimaryController() {
		if (primaryController == null)
			primaryController = XboxController.getInstance(Mappings.primaryControllerId.getValue());
		return primaryController;
	}

	public static XboxController getSecondaryController() {
		if (secondaryController == null)
			secondaryController = XboxController.getInstance(Mappings.secondaryControllerId.getValue());
		return secondaryController;
	}

	public static double getRightThrottle() { // We're doing tank drive to start
		return primaryController.getRightStickY();
	}

	public static double getLeftThrottle() { // We're doing tank drive to start
		return primaryController.getLeftStickY();
	}

	protected static void updateControllers() {
		primaryController = XboxController.getInstance(Mappings.primaryControllerId.getValue());
		secondaryController = XboxController.getInstance(Mappings.secondaryControllerId.getValue());
	}

}