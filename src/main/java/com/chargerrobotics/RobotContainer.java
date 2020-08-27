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

import com.chargerrobotics.commands.shooter.HoodCalibrateCommand;
import com.chargerrobotics.commands.shooter.HoodManualCommand;
import com.chargerrobotics.commands.shooter.HoodPIDCommand;
import com.chargerrobotics.commands.shooter.HoodPresetAngleCommand;
import com.chargerrobotics.commands.shooter.HoodRetractCommand;
import com.chargerrobotics.commands.shooter.KickerCommand;
import com.chargerrobotics.commands.shooter.ShooterOffCommand;
import com.chargerrobotics.commands.shooter.ShooterOnCommand;
import com.chargerrobotics.sensors.BallSensorSerial;
import com.chargerrobotics.sensors.ColorSensorSerial;
import com.chargerrobotics.sensors.GyroscopeSerial;
import com.chargerrobotics.sensors.ScaleSerial;
import com.chargerrobotics.commands.autonomous.AutoDriveLinear;
import com.chargerrobotics.commands.chomper.ChomperCalibrateCommand;
import com.chargerrobotics.commands.chomper.ChomperIntakeCommand;
import com.chargerrobotics.commands.chomper.ChomperPIDCommand;
import com.chargerrobotics.commands.chomper.ChomperUpDownCommand;
import com.chargerrobotics.commands.chomper.ChomperDownPIDCommand;
import com.chargerrobotics.commands.chomper.ChomperUpPIDCommand;
import com.chargerrobotics.commands.chomper.ChomperVomitCommand;
import com.chargerrobotics.commands.climber.ClimberDownCommand;
import com.chargerrobotics.commands.climber.ClimberUpCommand;
import com.chargerrobotics.commands.colorspinner.ColorSpinnerCommand;
import com.chargerrobotics.commands.colorspinner.ColorSpinnerDeploy;
import com.chargerrobotics.commands.colorspinner.ColorSpinnerRetract;
import com.chargerrobotics.commands.colorspinner.ColorTargetCommand;
import com.chargerrobotics.commands.drive.BoostCommand;
import com.chargerrobotics.commands.drive.BrakeCommand;
import com.chargerrobotics.commands.drive.ManualDriveCommand;
import com.chargerrobotics.commands.drive.SlowCommand;
import com.chargerrobotics.commands.feeder.FeederCommand;
import com.chargerrobotics.commands.groups.VisionDrive;
import com.chargerrobotics.commands.groups.VisionTurn;
import com.chargerrobotics.subsystems.ChomperSubsystem;
import com.chargerrobotics.subsystems.ClimberSubsystem;
import com.chargerrobotics.subsystems.ColorSpinnerSubsystem;
import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.subsystems.FeedSubsystem;
import com.chargerrobotics.subsystems.KickerSubsystem;
import com.chargerrobotics.subsystems.LEDSubsystem;
import com.chargerrobotics.subsystems.LimelightSubsystem;
import com.chargerrobotics.subsystems.ShooterHoodSubsystem;
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

	private static final boolean limelightEnabled = true;
	private static final boolean driveEnabled = true;
	private static final boolean chomperEnabled = true;
	private static final boolean feedEnabled = true;
	private static final boolean shooterEnabled = true;
	private static final boolean shooterHoodEnabled = true;
	private static final boolean colorSpinnerEnabled = false;
	private static final boolean climberEnabled = false;

	// Limelight
	private LimelightSubsystem limelightSubsystem;

	// Align to the target
	public VisionTurn alignToTarget;
	public VisionDrive driveToTarget;

	// Drive
	private DriveSubsystem driveSubsystem;
	private ManualDriveCommand manualDriveCommand;
	private BrakeCommand brakeCommand;
	private BoostCommand boostCommand;
	private SlowCommand slowCommand;
	private AutoDriveLinear autoDriveLinear;

	// Shooter
	private ShooterSubsystem shooterSubsystem;
	private KickerSubsystem kickerSubsystem;
	private ShooterHoodSubsystem shooterHoodSubsystem;
	private ShooterOnCommand shooterOnCommand;
	private ShooterOffCommand shooterOffCommand;
	private HoodManualCommand hoodManualUpCommand;
	private HoodManualCommand hoodManualDownCommand;
	private HoodCalibrateCommand hoodCalibrateCommand;
	private HoodPIDCommand hoodPIDCommand;
	private HoodPresetAngleCommand hoodPresetAngleCommand;
	private HoodRetractCommand hoodRetractCommand;
	private KickerCommand kickerCommand;

	// Chomper
	private ChomperSubsystem chomperSubsystem;
	private ChomperCalibrateCommand chomperCalibrateCommand;
	private ChomperIntakeCommand chomperIntakeCommand;
	private ChomperVomitCommand chomperVomitCommand;
	private ChomperUpPIDCommand chomperUpCommand;
	private ChomperDownPIDCommand chomperDownCommand;
	private ChomperUpDownCommand manualchomperUpCommand;
	private ChomperUpDownCommand manualchomperDownCommand;

	//Feeder
	private FeedSubsystem feedSubsystem;
	private FeederCommand feederCommand;

	// Color Spinner
	private ColorSpinnerSubsystem colorSpinnerSubsystem;
	private ColorSpinnerCommand colorSpinnerCommand;
	private ColorTargetCommand colorTargetCommand;
	private ColorSpinnerDeploy colorSpinnerDeploy;
	private ColorSpinnerRetract colorSpinnerRetract;

	// Climber Spinner
	private ClimberSubsystem climberSubsystem;
	private ClimberUpCommand climberUpCommand;
	private ClimberDownCommand climberDownCommand;

	// Serial connection
	public ColorSensorSerial colorSensor = new ColorSensorSerial();
	public ScaleSerial scaleSensor = new ScaleSerial();
	public GyroscopeSerial gyroscope = new GyroscopeSerial();
	public BallSensorSerial ballSensor = new BallSensorSerial();

	// LEDs
	public LEDSubsystem leds = new LEDSubsystem();

	// controllers
	public final static XboxController primary = new XboxController(Constants.primary);
	public final static XboxController secondary = new XboxController(Constants.secondary);

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		ArduinoSerialReceiver.initialization(() -> {
			ballSensor.resetCount();
		});
		Config.setup();
		if(feedEnabled) {
			feedSubsystem = FeedSubsystem.getInstance();
			feederCommand = new FeederCommand(feedSubsystem);
		}
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
			if (driveEnabled) {
				//Vision Testing
				alignToTarget = new VisionTurn(limelightSubsystem, driveSubsystem);
				driveToTarget = new VisionDrive(limelightSubsystem, driveSubsystem, 120);
			}
		}
		if (shooterEnabled) {
			shooterSubsystem = ShooterSubsystem.getInstance();
			shooterOnCommand = new ShooterOnCommand(shooterSubsystem);
			shooterOffCommand = new ShooterOffCommand(shooterSubsystem);
			kickerSubsystem = KickerSubsystem.getInstance();
			kickerCommand = new KickerCommand(kickerSubsystem, feedSubsystem);
		}
		if (shooterHoodEnabled) {
			shooterHoodSubsystem = ShooterHoodSubsystem.getInstance();
			hoodManualUpCommand = new HoodManualCommand(shooterHoodSubsystem, true);
			hoodManualDownCommand = new HoodManualCommand(shooterHoodSubsystem, false);
			hoodCalibrateCommand = new HoodCalibrateCommand(shooterHoodSubsystem);
			hoodPIDCommand = new HoodPIDCommand(shooterHoodSubsystem);
			hoodPresetAngleCommand = new HoodPresetAngleCommand(shooterHoodSubsystem, Constants.hoodPresetAngle);
			hoodRetractCommand = new HoodRetractCommand(shooterHoodSubsystem, Constants.hoodRetractAngle);
		}
		if(chomperEnabled) {
			chomperSubsystem = ChomperSubsystem.getInstance();
			chomperCalibrateCommand = new ChomperCalibrateCommand(chomperSubsystem);
			chomperIntakeCommand = new ChomperIntakeCommand(chomperSubsystem, feedSubsystem);
			chomperVomitCommand = new ChomperVomitCommand(chomperSubsystem);
			chomperUpCommand = new ChomperUpPIDCommand(true, chomperSubsystem);
			chomperDownCommand = new ChomperDownPIDCommand(false, chomperSubsystem);
			manualchomperUpCommand = new ChomperUpDownCommand(true);
			manualchomperDownCommand = new ChomperUpDownCommand(false);
		}
		if (colorSpinnerEnabled) {
			colorSpinnerSubsystem = ColorSpinnerSubsystem.getInstance();
			colorSpinnerCommand = new ColorSpinnerCommand(colorSpinnerSubsystem);
			colorTargetCommand = new ColorTargetCommand(colorSpinnerSubsystem, colorSensor);
			colorSpinnerDeploy = new ColorSpinnerDeploy();
			colorSpinnerRetract = new ColorSpinnerRetract();
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
			primary.buttonX.whileHeld(alignToTarget);
			primary.buttonY.whileHeld(driveToTarget);
		}
		if (climberEnabled) {
			climberSubsystem.setStop();
			primary.buttonPovUp.whileHeld(climberUpCommand);
			primary.buttonPovDown.whileHeld(climberDownCommand);
		}
		if (chomperEnabled) {
			primary.buttonPovUp.whenPressed(chomperUpCommand);
			primary.buttonPovDown.whenPressed(chomperDownCommand);
			primary.buttonMenu.whenPressed(chomperCalibrateCommand);
			primary.buttonPovRight.whileHeld(manualchomperUpCommand);
			
		}
		// secondary
		if (shooterEnabled) {
			secondary.buttonA.whenPressed(shooterOnCommand);
			secondary.buttonB.whenPressed(shooterOffCommand);
			secondary.buttonStickLeft.whileHeld(kickerCommand);
		}
		if (shooterHoodEnabled) {
			secondary.buttonMenu.whenPressed(hoodCalibrateCommand);
			secondary.buttonView.whenPressed(hoodPIDCommand);
			//secondary.buttonPovUp.whenPressed(hoodPresetAngleCommand);
			secondary.buttonPovUp.whileHeld(hoodManualUpCommand);
			secondary.buttonPovDown.whileHeld(hoodManualDownCommand);
		}
		if (chomperEnabled) {
			secondary.buttonBumperLeft.whileHeld(chomperIntakeCommand);
			secondary.buttonStickRight.whileHeld(chomperVomitCommand);
			//secondary.buttonBumperRight.whenPressed(chomperCalibrateCommand);
			//secondary.buttonView.whenPressed(chomperUpCommand);
			//secondary.buttonMenu.whenPressed(chomperDownCommand);
			//secondary.buttonA.whileHeld(manualchomperDownCommand);
			//secondary.buttonB.whileHeld(manualchomperUpCommand);
			//secondary.buttonX.whileHeld(manualchomperDownCommand);
			//secondary.buttonY.whileHeld(manualchomperUpCommand);
		}
		if (colorSpinnerEnabled) {
			secondary.buttonPovLeft.whenPressed(colorTargetCommand);
			secondary.buttonPovUp.whenPressed(colorSpinnerDeploy);
			secondary.buttonPovDown.whenPressed(colorSpinnerRetract);
		}
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
