/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.chomper;

import com.chargerrobotics.subsystems.ChomperSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ChomperIntakeCommand extends CommandBase {
  /**
   * Creates a new IntakeCommand.
   */
  private final ChomperSubsystem chomperSubsystem;

  public ChomperIntakeCommand(ChomperSubsystem chomperSubsystem) {
    this.chomperSubsystem = chomperSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    chomperSubsystem.setChomperFeedRunning(true);
    SmartDashboard.putString("Chomper Intake", "Running");
  }

  // Called every time the scheduler runs whiley the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    chomperSubsystem.setChomperFeedRunning(false);
    SmartDashboard.putString("Chomper Intake", "Stopped");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
