/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodPresetAngleCommand extends CommandBase {
  /**
   * Creates a new HoodPresetAngleCommand.
   * 
   * Moves the hood to a preset angle specified in constants.  Will be used 
   * until a mathmatical curve can be identified that can reliably set
   * the hood angle at any reasonable distance.
   * 
   * TODO:  create constant for preset angle.
   */
  public HoodPresetAngleCommand() {
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
