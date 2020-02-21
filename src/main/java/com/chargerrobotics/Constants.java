/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
	// Controller IDs
	public static final int primary = 0;
	public static final int secondary = 1;

	// CAN Id's
	// 1: Power distribution panel (PDP)
	// 2: Pneumatic Control Module (PCM)
	// 1x: Drive: Odds are left, evens are right
	// 2x: Shooter
	// 3x: Color Spinner
	public static final int powerDistributionPanel = 1;
	public static final int pneumaticControlModule = 2;
	public static final int rightFrontDrive = 11;
	public static final int rightRearDrive = 12;
	public static final int leftFrontDrive = 13;
	public static final int leftRearDrive = 14;
	public static final int shooterID1 = 21;
	public static final int shooterID2 = 22;
	public static final int colorSpinner = 31;

	// Shooter Constants
	public static final double shooterTargetRPM = 10000.0;
	public static final double shooterP = 0.0002;
	public static final double shooterI = 0.0;
	public static final double shooterD = 0.0;
	public static final double shooterFeedForward = 0.0;
	public static final double shooterStaticGain = 0.0;
	public static final double shooterVelocityGain = 0.0;
	public static final double shooterMinOutput = -1.0;
	public static final double shooterMaxOutput = 1.0;

	// File Names
	public static final String dataStoragePath = "/home/lvuser";
	public static final String configFileName = "config.yml";

	// Limelight distance calculation constants
	// Blitzen config:
	public static final double targetHeight = 94.0; // inches
	public static final double cameraHeight = 24.0; // inches
	public static final double cameraAngle = 30.0; // degrees

	// 2020 bot config:
	// public static final double targetHeight = 0.0; // inches
	// public static final double cameraHeight = 0.0; // inches
	// public static final double cameraAngle = 0.0; // degrees
}
