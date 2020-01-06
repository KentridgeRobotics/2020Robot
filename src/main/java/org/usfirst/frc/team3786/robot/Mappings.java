package org.usfirst.frc.team3786.robot;

import org.usfirst.frc.team3786.robot.OI;
import org.usfirst.frc.team3786.robot.utils.XboxController;
import org.usfirst.frc.team3786.robot.utils.config.Mapping;

public class Mappings {
	
	// Controller IDs
	public final static Mapping<Integer> primaryControllerId = new Mapping<Integer>("primary_controller", 0, () -> {OI.updateControllers();});
	public final static Mapping<Integer> secondaryControllerId = new Mapping<Integer>("secondary_controller", 1, () -> {OI.updateControllers();});
	public final static Mapping<Double> controllerDeadzone = new Mapping<Double>("controller_deadzone", 0.13, value -> {XboxController.setDeadzone(value);});

	// CAN IDs
	public final static Mapping<Integer> leftMotorId = new Mapping<Integer>("left_motor", 1, () -> {Robot.drive.setupMotors();});
	public final static Mapping<Integer> rightMotorId = new Mapping<Integer>("right_motor", 2, () -> {Robot.drive.setupMotors();});

	// Other
	public final static Mapping<Double> driveRampRate = new Mapping<Double>("drive_ramp_rate", 0.4, value -> {Robot.drive.setRampRate(value);});

	// Digital IO

	// Analog IO

	// Location for Data Storage
	public final static String dataStoragePath = "/home/lvuser";
	// public final static String dataStoragePath = "/media/sda1";
	public final static String configFileName = "config.yml";
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
		// primary.buttonBumperRight.whenPressed(new
		// DebugMotorControllerIncrement());
		// primary.buttonBumperLeft.whenPressed(new
		// DebugMotorControllerDecrement());
	}

}