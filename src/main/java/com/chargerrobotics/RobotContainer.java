/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.chargerrobotics.commands.ExampleCommand;
import com.chargerrobotics.commands.shooter.ShooterOffCommand;
import com.chargerrobotics.commands.shooter.ShooterOnCommand;
import com.chargerrobotics.subsystems.ExampleSubsystem;
import com.chargerrobotics.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final ShooterSubsystem shooterSubsystem = ShooterSubsystem.getInstance();

  // The robot's commands are defined here...
  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);
  private final ShooterOnCommand shooterOnCommand = new ShooterOnCommand(shooterSubsystem);
  private final ShooterOffCommand shooterOffCommand = new ShooterOffCommand(shooterSubsystem);



  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    setupDashboardValues();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    com.chargerrobotics.utils.XboxController primary = new com.chargerrobotics.utils.XboxController(Constants.primary);
    primary.buttonA.whenPressed(shooterOnCommand);
    primary.buttonB.whenPressed(shooterOffCommand);

    com.chargerrobotics.utils.XboxController secondary = new com.chargerrobotics.utils.XboxController(Constants.secondary);
    
  }

  public static final double kP = 5e-5;
  public static final double kI = 1e-6;
  public static final double kD = 0;
  public static final double kF = 0;
  public static final double kRpmSetpoint = 1000;

  private void setupDashboardValues() {

    SmartDashboard.putNumber("GainP", kP);
    SmartDashboard.putNumber("GainI", kI);
    SmartDashboard.putNumber("GainD", kD);
    SmartDashboard.putNumber("GainF", kF);
    SmartDashboard.putNumber("RpmSetpoint", kRpmSetpoint);

  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
