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
	public static final int colorSpinnerLifter = 9;
	public static final int leds = 1;

	//Digital In/Out Ports
	public static final int chomperLimitSwitch = 9;
	public static final int hoodLimitSwitch = 1;

	//Chomper Constants
	public static final int chomperDistToDown = 550;
	public static final int chomperDistBottomToUp = 1999;

	// Shooter Constants
	public static final double shooterTargetRPM = 2500.0;
	public static final double shooterP = 0.0005;
	public static final double shooterI = 0.00000047;
	public static final double shooterD = 0.014;
	public static final int shooterCurrentLimit = 40;
	public static final double shooterFeedForward = 0.0;
	public static final double shooterStaticGain = 0.0;
	public static final double shooterVelocityGain = 0.0;
	public static final double shooterMinOutput = -1.0;
	public static final double shooterMaxOutput = 1.0;
	
	public static final double desiredDistance = 120.0;

	// Shooter Hood Constants
	public static final int hoodPIDLoopId = 0;
	public static final int hoodGainSlot = 0;
	public static final int hoodTimeOutMs = 30;
	public static final int ticksPerRev = 0;
	public static final double hoodP = 0.0007;
	public static final double hoodI = 0.00038;
	public static final double hoodD = 0.0;
	public static final double hoodF = 0.0;

	public static final double hoodPresetAngle = 500;
	public static final double hoodRetractAngle = 1800;
	public static final double defaultHoodSpeed = 0.30;


	// File Names
	public static final String dataStoragePath = "/home/lvuser";
	public static final String configFileName = "config.yml";

	// Limelight distance calculation constants
	// Blitzen config:
	//public static final double targetHeight = 94.0; // inches
	//public static final double cameraHeight = 24.0; // inches
	//public static final double cameraAngle = 30.0; // degrees

	// 2020 bot config:
	public static final double targetHeight = 91.5; // inches
	public static final double cameraHeight = 23.75; // inches
	public static final double cameraAngle = 29.0; // degrees
	public static final String comPortsFileName = "com.yml";

	/*
	Tests at Tahoma
	Front bumper on the line: 3000 RPM, 55 degrees hood, distance 131 inches
	*/
}
