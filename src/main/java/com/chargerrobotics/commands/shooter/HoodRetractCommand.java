/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.chargerrobotics.subsystems.ShooterHoodSubsystem;

public class HoodRetractCommand extends CommandBase {
  private final ShooterHoodSubsystem shooterHoodSubsystem;
  private final double hoodRetractSetPoint;
  /**
   * Creates a new HoodRetractCommand.
   */
  public HoodRetractCommand(ShooterHoodSubsystem shooterHoodSubsystem, double hoodRetractSetPoint) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.shooterHoodSubsystem = shooterHoodSubsystem;
    this.hoodRetractSetPoint = hoodRetractSetPoint;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //shooterHoodSubsystem.setPosition(hoodRetractSetPoint);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
