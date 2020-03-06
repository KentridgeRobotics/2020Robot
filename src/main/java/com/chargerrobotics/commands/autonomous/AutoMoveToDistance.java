/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.autonomous;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.chargerrobotics.subsystems.LimelightSubsystem;

public class AutoMoveToDistance extends CommandBase {
  /**
   * Creates a new AutoMoveToDistance.
   * 
   * Until we figure out how to fit the hood angle to a curve as f(distance) this 
   * command will move the robot forward/back to a specified distance using 
   * the distance method in the LimeLightSubsystem.
   * 
   * The distance should be far enough to "exit the infinite verticle volume"
   * referenced in section 4.2 of the game manual.
   * 
   * TODO:  create a constant for the target distance
   */
  public AutoMoveToDistance() {
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
