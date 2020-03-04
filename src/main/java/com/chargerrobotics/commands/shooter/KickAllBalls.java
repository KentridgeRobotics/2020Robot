/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class KickAllBalls extends CommandBase {
  /**
   * Creates a new KickAllBalls.
   * 
   * Kicks all balls in magazine until empty.  Assumes that robot will be in the same
   * position and in alignment with the target from first ball to last ball.
   * 
   * Made this a seperate command because of the interplay with the ball sensors
   * and that you don't want the shooter to stop/start between each ball.  Also 
   * consider that there may be 1-5 balls in the magazine.  We shouldn't assume
   * there will only be 5 because of the limited number of balls on the field.
   * 
   * Also consider the feeder needs to be included in this to keep the balls moving.
   */
  public KickAllBalls() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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
