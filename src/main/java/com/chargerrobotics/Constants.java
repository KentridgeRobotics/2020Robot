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
	public static final int rightFrontDrive = 11;
	public static final int rightRearDrive = 12;
	public static final int leftRearDrive = 13;
	public static final int leftFrontDrive = 14;
	public static final int shooterLeft = 21;
	public static final int shooterRight = 22;
	public static final int shooterHood = 23;
	public static final int colorSpinner = 31;
	public static final int chomperFeed = 41;
	public static final int chomperLift = 42;
  	public static final int feedStage = 43;
	public static final int kicker = 44;
	public static final int climbExtender = 51;
	public static final int climbPush2 = 52;
	public static final int climbPull = 53;
	//PWM IDs
	public static final int colorSpinnerLifter = 0;

	// Shooter Constants
	public static final double shooterTargetRPM = 3000.0;
	public static final double shooterP = 0.001;
	public static final double shooterI = 0.0000004;
	public static final double shooterD = 0.2;
	public static final int shooterCurrentLimit = 40;
	public static final double shooterFeedForward = 0.0;
	public static final double shooterStaticGain = 0.0;
	public static final double shooterVelocityGain = 0.0;
	public static final double shooterMinOutput = -1.0;
	public static final double shooterMaxOutput = 1.0;

	//Chomper Constants
	public static final double chomperP = 0.1;
	public static final double chomperI = 0.0;
	public static final double chomperD = 0.0;
	public static final int chomperDownPosition = 100;

	// File Names
	public static final String dataStoragePath = "/home/lvuser";
	public static final String configFileName = "config.yml";
	public static final String comPortsFileName = "com.yml";
}
