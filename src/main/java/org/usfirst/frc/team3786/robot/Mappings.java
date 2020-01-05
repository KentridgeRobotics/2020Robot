package org.usfirst.frc.team3786.robot;

import org.usfirst.frc.team3786.robot.OI;
import org.usfirst.frc.team3786.robot.utils.XboxController;

public class Mappings {
	// Controller IDs
	public final static int primaryControllerId = 0;
	public final static int secondaryControllerId = 1;

	// CAN IDs
	public final static int leftMotor = 1;
	public final static int rightMotor = 2;

	// Digital IO

	// Analog IO

	// Flash Drive Location for Data Storage
	public final static String dataStoragePath = "/home/lvuser";
	// public final static String dataStoragePath = "/media/sda1";
	public final static String gyroCalibrationFileName = "gyroCalibration.dat";

	public static void setupDefaultMappings() {

		@SuppressWarnings("unused")
		XboxController primary = OI.getPrimaryController();
		// primary.buttonA.whileHeld(new RollersBackwardCommand());

		@SuppressWarnings("unused")
		XboxController secondary = OI.getSecondaryController();
	}

	public static void setupTestMappings() {
		@SuppressWarnings("unused")
		XboxController primary = OI.getPrimaryController();
		// primary.buttonBumperRight.whenPressed(new DebugMotorControllerIncrement());
		// primary.buttonBumperLeft.whenPressed(new DebugMotorControllerDecrement());
	}

}