/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3786.robot;

import java.util.Arrays;

import org.usfirst.frc.team3786.robot.commands.DriveCommand;
import org.usfirst.frc.team3786.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team3786.robot.subsystems.NeoDriveSubsystem;
import org.usfirst.frc.team3786.robot.subsystems.vision.Cameras;
import org.usfirst.frc.team3786.robot.utils.Gyroscope;
import org.usfirst.frc.team3786.robot.utils.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	public static final int TEAM = 3786;

	public static Robot instance;

	public static DriveSubsystem drive;
	public static Command driveCommand;
	public static Gyroscope gyro;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		Config.setup();
		Mappings.setupDefaultMappings();
		gyro = Gyroscope.getInstance();
		drive = NeoDriveSubsystem.getInstance();
		driveCommand = DriveCommand.getInstance();
		Cameras.setup();
	}

	/**
	 * This function is called every robot packet, no matter the mode. Use this
	 * for items like diagnostics that you want ran during disabled, autonomous,
	 * teleoperated and test.
	 *
	 * This runs after the mode specific periodic functions, but before
	 * LiveWindow and SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic() {
		Scheduler.getInstance().run();
		gyro.run();
		SmartDashboard.putNumber("current_heading", Gyroscope.getInstance().getHeadingContinuous());
	}

	/**
	 * This function is called when entering teleop.
	 */
	@Override
	public void teleopInit() {
		driveCommand.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
	}

	/**
	 * This function is called when entering autonomous.
	 */
	@Override
	public void autonomousInit() {
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
	}

	/**
	 * This function is called when disabling.
	 */
	@Override
	public void disabledInit() {
	}

	/**
	 * This function is called periodically while disabled.
	 */
	@Override
	public void disabledPeriodic() {
	}

	/**
	 * This function is called when entering test mode.
	 */
	@Override
	public void testInit() {
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public ControlPanelColor getControlPanelColor() {
		String data = DriverStation.getInstance().getGameSpecificMessage();
		return data.length() > 0 ? ControlPanelColor.valueOf(data.charAt(0)) : null;
	}

	private enum ControlPanelColor {
		BLUE('B'), GREEN('G'), RED('R'), YELLOW('Y');

		private final int id;

		private ControlPanelColor(char c) {
			this.id = c;
		}

		public static ControlPanelColor valueOf(char c) {
			return Arrays.stream(values()).filter(color -> color.id == c).findFirst().orElse(null);
		}
	}

	/**
	 * DO NOT MODIFY
	 */
	private Robot() {
		Robot.instance = this;
	}

	/**
	 * DO NOT MODIFY
	 */
	public static void main(String... args) {
		RobotBase.startRobot(Robot::new);
	}
}
