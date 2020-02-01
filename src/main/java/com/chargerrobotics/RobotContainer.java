/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import com.chargerrobotics.commands.shooter.ShooterOffCommand;
import com.chargerrobotics.commands.shooter.ShooterOnCommand;
import com.chargerrobotics.commands.LimelightCommand;
import com.chargerrobotics.commands.ClimberDownCommand;
import com.chargerrobotics.commands.ClimberUpCommand;
import com.chargerrobotics.commands.colorspinner.ColorSpinnerCommand;
import com.chargerrobotics.commands.drive.BrakeCommand;
import com.chargerrobotics.commands.drive.ManualDriveCommand;
import com.chargerrobotics.subsystems.ClimberSubsystem;
import com.chargerrobotics.subsystems.ColorSpinnerSubsystem;
import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.subsystems.ShooterSubsystem;
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

	private static final boolean driveEnabled = true;
	private static final boolean shooterEnabled = true;
	private static final boolean colorSpinnerEnabled = true;
	private static final boolean climberEnabled = true;

	private LimelightCommand limelightCommand;

	// Drive
	private DriveSubsystem driveSubsystem;
	private ManualDriveCommand manualDriveCommand;
	private BrakeCommand brakeCommand;

	// Shooter
	private ShooterSubsystem shooterSubsystem;
	private ShooterOnCommand shooterOnCommand;
	private ShooterOffCommand shooterOffCommand;

	// Color Spinner
	private ColorSpinnerSubsystem colorSpinnerSubsystem;
	private ColorSpinnerCommand colorSpinnerCommand;

	// Climber Spinner
	private ClimberSubsystem climberSubsystem;
	private ClimberUpCommand climberUpCommand;
	private ClimberDownCommand climberDownCommand;

	private final Compressor compressor = new Compressor(Constants.pneumaticControlModule);

	// controllers
	public final static XboxController primary = new XboxController(Constants.primary);
	public final static XboxController secondary = new XboxController(Constants.secondary);

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		Config.setup();
		setupBindings();
		setupCamera();
		compressor.setClosedLoopControl(true);
		limelightCommand = new LimelightCommand();
		if (driveEnabled) {
			driveSubsystem = DriveSubsystem.getInstance();
			manualDriveCommand = new ManualDriveCommand(driveSubsystem);
			brakeCommand = new BrakeCommand(driveSubsystem);
		}
		if (shooterEnabled) {
			shooterSubsystem = ShooterSubsystem.getInstance();
			shooterOnCommand = new ShooterOnCommand(shooterSubsystem);
			shooterOffCommand = new ShooterOffCommand(shooterSubsystem);
		}
		if (colorSpinnerEnabled) {
			colorSpinnerSubsystem = ColorSpinnerSubsystem.getInstance();
			colorSpinnerCommand = new ColorSpinnerCommand(colorSpinnerSubsystem);
		}
		if (climberEnabled) {
			climberSubsystem = ClimberSubsystem.getInstance();
			climberUpCommand = new ClimberUpCommand(climberSubsystem);
			climberDownCommand = new ClimberDownCommand(climberSubsystem);
		}
	}

	public void setupCamera() {
		UsbCamera cam = CameraServer.getInstance().startAutomaticCapture();
		cam.setConnectVerbose(0);
		CvSink cvSink = CameraServer.getInstance().getVideo();
		CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
	}

	/**
	 * Use this method to define your button->command mappings. Buttons can be
	 * created by instantiating a {@link GenericHID} or one of its subclasses
	 * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
	 * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
	 */
	private void setupBindings() {
		// primary
		primary.buttonB.whileHeld(brakeCommand);
		primary.buttonY.whileHeld(limelightCommand);
		primary.buttonPovUp.whileHeld(climberUpCommand);
		primary.buttonPovDown.whileHeld(climberDownCommand);
		//secondary
		secondary.buttonA.whenPressed(shooterOnCommand);
		secondary.buttonB.whenPressed(shooterOffCommand);
	}

}
