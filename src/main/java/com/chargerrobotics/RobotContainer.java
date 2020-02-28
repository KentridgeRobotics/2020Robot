/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import com.chargerrobotics.commands.shooter.ShooterOffCommand;
import com.chargerrobotics.commands.shooter.ShooterOnCommand;
import com.chargerrobotics.sensors.BallSensorSerial;
import com.chargerrobotics.sensors.ColorSensorSerial;
import com.chargerrobotics.sensors.GyroscopeSerial;
import com.chargerrobotics.sensors.ScaleSerial;
import com.chargerrobotics.commands.LimelightCommand;
import com.chargerrobotics.commands.autonomous.AutoDriveLinear;
import com.chargerrobotics.commands.autonomous.VisionTurn;
import com.chargerrobotics.commands.chomper.ChomperIntakeCommand;
import com.chargerrobotics.commands.chomper.ChomperPIDCommand;
import com.chargerrobotics.commands.climber.ClimberDownCommand;
import com.chargerrobotics.commands.climber.ClimberUpCommand;
import com.chargerrobotics.commands.colorspinner.ColorSpinnerCommand;
import com.chargerrobotics.commands.colorspinner.ColorTargetCommand;
import com.chargerrobotics.commands.drive.BoostCommand;
import com.chargerrobotics.commands.drive.BrakeCommand;
import com.chargerrobotics.commands.drive.ManualDriveCommand;
import com.chargerrobotics.commands.drive.SlowCommand;
import com.chargerrobotics.subsystems.ChomperSubsystem;
import com.chargerrobotics.subsystems.ClimberSubsystem;
import com.chargerrobotics.subsystems.ColorSpinnerSubsystem;
import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.subsystems.LimelightSubsystem;
import com.chargerrobotics.subsystems.ShooterSubsystem;
import com.chargerrobotics.utils.ArduinoSerialReceiver;
import com.chargerrobotics.utils.Config;
import com.chargerrobotics.utils.XboxController;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

	private static final boolean limelightEnabled = false;
	private static final boolean driveEnabled = false;
	private static final boolean chomperEnabled = true;
	private static final boolean shooterEnabled = false;
	private static final boolean colorSpinnerEnabled = false;
	private static final boolean climberEnabled = false;

	// Limelight
	private LimelightSubsystem limelightSubsystem;
	private LimelightCommand limelightCommand;
	// Align to the target
	public VisionTurn alignToTarget;

	// Drive
	private DriveSubsystem driveSubsystem;
	private ManualDriveCommand manualDriveCommand;
	private BrakeCommand brakeCommand;
	private BoostCommand boostCommand;
	private SlowCommand slowCommand;
	private AutoDriveLinear autoDriveLinear;

	// Shooter
	private ShooterSubsystem shooterSubsystem;
	private ShooterOnCommand shooterOnCommand;
	private ShooterOffCommand shooterOffCommand;

	// Chomper
	private ChomperSubsystem chomperSubsystem;
	private ChomperIntakeCommand chomperIntakeCommand;
	private ChomperPIDCommand chomperUpCommand; 
	private ChomperPIDCommand chomperDownCommand; 

	// Color Spinner
	private ColorSpinnerSubsystem colorSpinnerSubsystem;
	private ColorSpinnerCommand colorSpinnerCommand;
	private ColorTargetCommand colorTargetCommand;

	// Climber Spinner
	private ClimberSubsystem climberSubsystem;
	private ClimberUpCommand climberUpCommand;
	private ClimberDownCommand climberDownCommand;

	// Serial connection
	public ColorSensorSerial colorSensor = new ColorSensorSerial();
	public ScaleSerial scaleSensor = new ScaleSerial();
	public GyroscopeSerial gyroscope = new GyroscopeSerial();
	public BallSensorSerial ballSensor = new BallSensorSerial();

	// controllers
	public final static XboxController primary = new XboxController(Constants.primary);
	public final static XboxController secondary = new XboxController(Constants.secondary);

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
//		ArduinoSerialReceiver.initialization(() -> {
//			ballSensor.resetCount();
//		});
		Config.setup();
		if (driveEnabled) {
			driveSubsystem = DriveSubsystem.getInstance();
			manualDriveCommand = new ManualDriveCommand(driveSubsystem);
			brakeCommand = new BrakeCommand(driveSubsystem);
			boostCommand = new BoostCommand(driveSubsystem);
			slowCommand = new SlowCommand(driveSubsystem);
			autoDriveLinear = new AutoDriveLinear(driveSubsystem, gyroscope);
		}
		if (limelightEnabled) {
			limelightSubsystem = LimelightSubsystem.getInstance();
			limelightCommand = new LimelightCommand(limelightSubsystem);
			limelightSubsystem.setRunning(true);
			if (driveEnabled) {
				//Vision Testing
				alignToTarget = new VisionTurn(limelightSubsystem, driveSubsystem);
			}
		}
		if (shooterEnabled) {
			shooterSubsystem = ShooterSubsystem.getInstance();
			shooterOnCommand = new ShooterOnCommand(shooterSubsystem);
			shooterOffCommand = new ShooterOffCommand(shooterSubsystem);
		}
		if(chomperEnabled) {
			chomperSubsystem = ChomperSubsystem.getInstance();
			chomperIntakeCommand = new ChomperIntakeCommand(chomperSubsystem);
			chomperUpCommand = new ChomperPIDCommand(50, chomperSubsystem);
			chomperDownCommand = new ChomperPIDCommand(0, chomperSubsystem);

		}
		if (colorSpinnerEnabled) {
			colorSpinnerSubsystem = ColorSpinnerSubsystem.getInstance();
			colorSpinnerCommand = new ColorSpinnerCommand(colorSpinnerSubsystem);
			colorTargetCommand = new ColorTargetCommand(colorSpinnerSubsystem, colorSensor);
		}
		if (climberEnabled) {
			climberSubsystem = ClimberSubsystem.getInstance();
			climberUpCommand = new ClimberUpCommand(climberSubsystem);
			climberDownCommand = new ClimberDownCommand(climberSubsystem);
		}
		setupBindings();
		setupCamera();
	}

	public void setupCamera() {
		UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
		cam.setConnectVerbose(0);
	}

	/**
	 * Use this method to define your button->command mappings. Buttons can be
	 * created by instantiating a {@link GenericHID} or one of its subclasses
	 * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
	 * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	 */
	private void setupBindings() {
		// primary
		if (driveEnabled) {
			primary.buttonB.whileHeld(brakeCommand);
			primary.buttonBumperRight.whileHeld(boostCommand);
			primary.buttonBumperLeft.whileHeld(slowCommand);
			primary.buttonPovLeft.whenPressed(autoDriveLinear);
		}
		if (limelightEnabled) {
			primary.buttonY.whileHeld(alignToTarget);
		}
		if (climberEnabled) {
			climberSubsystem.setStop();
			primary.buttonPovUp.whileHeld(climberUpCommand);
			primary.buttonPovDown.whileHeld(climberDownCommand);
		}
		// secondary
		if (shooterEnabled) {
			secondary.buttonA.whenPressed(shooterOnCommand);
			secondary.buttonB.whenPressed(shooterOffCommand);
		}
		if (chomperEnabled) {
			secondary.buttonBumperLeft.whileHeld(chomperIntakeCommand);
		}
		if (colorSpinnerEnabled) {
			secondary.buttonX.whenPressed(colorTargetCommand);
		}
		// secondary.buttonX.whenPressed(chomperCommand);
	}

	public void setDefaultDriveCommand() {
		if (driveEnabled) {
			CommandScheduler.getInstance().setDefaultCommand(driveSubsystem, manualDriveCommand);
		}
	}

	public void setTeleop() {
		if (driveEnabled) {
			manualDriveCommand.schedule();
			if (limelightEnabled)
				limelightSubsystem.setLEDStatus(false);
		}
	}

	public void setAutonomous() {
		if (driveEnabled && limelightEnabled) {
			limelightSubsystem.setLEDStatus(true);
			alignToTarget.schedule();
		}
	}

	public void setDisabled() {
		if (limelightEnabled)
			limelightSubsystem.setLEDStatus(false);
	}

}
