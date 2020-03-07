/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.chargerrobotics.subsystems.ShooterHoodSubsystem;

public class HoodPresetAngleCommand extends CommandBase {
  private final ShooterHoodSubsystem shooterHoodSubsystem;
  private final double hoodSetPoint;
  /**
   * Creates a new HoodPresetAngleCommand.
   */
  public HoodPresetAngleCommand(ShooterHoodSubsystem shooterHoodSubsystem, double hoodSetPoint) {
    this.shooterHoodSubsystem = shooterHoodSubsystem;
    this.hoodSetPoint = hoodSetPoint;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //shooterHoodSubsystem.setPosition(hoodSetPoint);
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
