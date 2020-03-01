/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.groups;

import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.subsystems.LimelightSubsystem;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.PIDCommand;

public class VisionDrive extends PIDCommand {
  /**
   * Creates a new VisionDrive.
   */
  private static LimelightSubsystem limelightSubsystem;
  private static DriveSubsystem driveSubsystem;

  public VisionDrive(final LimelightSubsystem limelightSubsystem, DriveSubsystem driveSubsystem, double desiredDistance) {
    super(
      new PIDController(0.004, 0, 0),
      () -> limelightSubsystem.distance(),
      () -> desiredDistance,
      output -> {
        System.out.println(output);
        driveSubsystem.arcadeDrive(output, 0);
      });

    this.limelightSubsystem = limelightSubsystem;
    this.driveSubsystem = driveSubsystem;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    super.execute();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    super.isFinished();
    return false;
  }
}
