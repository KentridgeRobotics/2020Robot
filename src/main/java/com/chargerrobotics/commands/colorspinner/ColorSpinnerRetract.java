/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.colorspinner;

import com.chargerrobotics.subsystems.ColorSpinnerSubsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ColorSpinnerRetract extends CommandBase {
  private final static Logger logger = LoggerFactory.getLogger(ColorSpinnerRetract.class);
  /**
   * Creates a new ColorSpinnerRetract.
   */
  public ColorSpinnerRetract() {
    addRequirements(ColorSpinnerSubsystem.getInstance());
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("initialize");
//    ColorSpinnerSubsystem.getInstance().setAngle(0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    logger.info("end");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
