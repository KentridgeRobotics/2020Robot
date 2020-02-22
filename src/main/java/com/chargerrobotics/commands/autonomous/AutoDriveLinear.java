/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.autonomous;

import com.chargerrobotics.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoDriveLinear extends CommandBase {
  private double initialLeftDistance;
  private double initialRightDistance;
  private double currentLeftDistance;
  private double currentRightDistance;
  private double initialGyroAngle;
  private double desiredDistance;
  private final DriveSubsystem drive;
  private DifferentialDriveOdometry odometry;
  private static final String KEY = "LinearAutoDistance";
  private PIDController rotationPid;
  private PIDController translationPid;
  /**
   * Creates a new AutoDriveLinear.
   */
  public AutoDriveLinear(DriveSubsystem drive) {
    this.drive = drive;
    addRequirements(drive);
    SmartDashboard.putNumber(KEY, 0.0);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initialLeftDistance = drive.getOdoLeft();
    initialRightDistance = drive.getOdoRight();
    currentLeftDistance = initialLeftDistance;
    currentRightDistance = initialRightDistance;
    desiredDistance = SmartDashboard.getNumber(KEY, 0.0);
    rotationPid = new PIDController(
      SmartDashboard.getNumber("linRotP", 0.0),
      SmartDashboard.getNumber("linRotI", 0.0),
      SmartDashboard.getNumber("linRotD", 0.0)
    );
    translationPid = new PIDController(
      SmartDashboard.getNumber("linTransP", 0.0),
      SmartDashboard.getNumber("linTransI", 0.0),
      SmartDashboard.getNumber("linTransD", 0.0)
    );
    odometry = new DifferentialDriveOdometry(getGyroHeading(), new Pose2d(desiredDistance, 0.0, new Rotation2d(0.0)));
  }

  private double getDistanceLeft() {
    return currentLeftDistance - initialLeftDistance;
  }

  private double getDistanceRight() {
    return currentRightDistance - initialRightDistance;
  }

  private Rotation2d getGyroHeading() {
    double newHeading = 
    return null;

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentLeftDistance = drive.getOdoLeft();
    currentRightDistance = drive.getOdoRight();

    Pose2d pose = odometry.update(getGyroHeading(), getDistanceLeft(), getDistanceRight());
    Rotation2d rotation = pose.getRotation();
    Translation2d translation = pose.getTranslation();
    double rotationOutput = rotationPid.calculate(rotation.getDegrees(), 0.0);
    double translationOutput = translationPid.calculate(translation.getNorm());

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
