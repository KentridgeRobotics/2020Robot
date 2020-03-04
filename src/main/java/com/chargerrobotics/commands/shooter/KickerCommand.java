/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.FeedSubsystem;
import com.chargerrobotics.subsystems.KickerSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class KickerCommand extends CommandBase {
  private final FeedSubsystem feedSubsystem;
  /**
   * Creates a new KickerCommand.
   */
  private final KickerSubsystem kickerSubsystem;

  public KickerCommand(KickerSubsystem kickerSubsystem, FeedSubsystem feedSubsystem) {
    this.kickerSubsystem = kickerSubsystem;
    this.feedSubsystem = feedSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    feedSubsystem.setFeedRunning(true);
    SmartDashboard.putNumber("KickerMotor", 0.1);
    kickerSubsystem.setRunning(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    kickerSubsystem.setRunning(false);
    feedSubsystem.setFeedRunning(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
