/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.autonomous;

import com.chargerrobotics.sensors.GyroscopeSerial;
import com.chargerrobotics.subsystems.DriveSubsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoDriveLinear extends CommandBase {
  private static final Logger logger = LoggerFactory.getLogger(AutoDriveLinear.class);
  private double initialLeftDistance;
  private double initialRightDistance;
  private double currentLeftDistance;
  private double currentRightDistance;
  private double currentHeading;
  private double initialHeading;
  private double desiredDistance;
  private final DriveSubsystem drive;
  private final GyroscopeSerial gyro;
  private DifferentialDriveOdometry odometry;
  private static final String KEY = "LinearAutoDistance";
  private PIDController rotationPid;
  private PIDController translationPid;
  private double linRotP = SmartDashboard.getNumber("linRotP", 0.0);
  private double linRotI = SmartDashboard.getNumber("linRotI", 0.0);
  private double linRotD = SmartDashboard.getNumber("linRotD", 0.0);
  private double linRotTolerance = SmartDashboard.getNumber("linRotTolerance", 0.0);
  private double linTransP = SmartDashboard.getNumber("linTransP", 0.0);
  private double linTransI = SmartDashboard.getNumber("linTransI", 0.0);
  private double linTransD = SmartDashboard.getNumber("linTransD", 0.0);
  private double linTransTolerance = SmartDashboard.getNumber("linTransTolerance", 0.0);
  /**
   * Creates a new AutoDriveLinear.
   */
  public AutoDriveLinear(DriveSubsystem drive, GyroscopeSerial gyro) {
    this.drive = drive;
    this.gyro = gyro;
    addRequirements(drive);
    SmartDashboard.putNumber(KEY, 0.0);
    SmartDashboard.putNumber("linRotP", linRotP);
    SmartDashboard.putNumber("linRotI", linRotI); 
    SmartDashboard.putNumber("linRotD",linRotD);
    SmartDashboard.putNumber("linRotTolerance", linRotTolerance);
    SmartDashboard.putNumber("linTransP", linTransP);
    SmartDashboard.putNumber("linTransI", linTransI);
    SmartDashboard.putNumber("linTransD", linTransD);
    SmartDashboard.putNumber("linTransTolerance", linTransTolerance);
    this.rotationPid = new PIDController(0.0, 0.0, 0.0);
    this.rotationPid.enableContinuousInput(0.0, 360.0);
    this.translationPid = new PIDController(0.0, 0.0, 0.0);
    
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    logger.info("AutoDrive starting");
    initialLeftDistance = drive.getOdoLeft();
    initialRightDistance = drive.getOdoRight();
    currentLeftDistance = initialLeftDistance;
    currentRightDistance = initialRightDistance;
    initialHeading = gyro.getHeading();
    currentHeading = initialHeading;
    desiredDistance = SmartDashboard.getNumber(KEY, 0.0);
    drive.setAutonomousRunning(true);
    drive.setThrottle(0, 0); // Clear out any current throttle on the drive....
    rotationPid.setP(SmartDashboard.getNumber("linRotP", 0.0));
    rotationPid.setI(SmartDashboard.getNumber("linRotI", 0.0));
    rotationPid.setD(SmartDashboard.getNumber("linRotD", 0.0));
    rotationPid.setTolerance(SmartDashboard.getNumber("linRotTol", 1.0));
    rotationPid.setSetpoint(0.0);
    translationPid.setP(SmartDashboard.getNumber("linTransP", 0.0));
    translationPid.setI(SmartDashboard.getNumber("linTransI", 0.0));
    translationPid.setD(SmartDashboard.getNumber("linTransD", 0.0));
    translationPid.setSetpoint(desiredDistance);
    translationPid.setTolerance(SmartDashboard.getNumber("linTransTolerance", 1.0));
    odometry = new DifferentialDriveOdometry(getGyroHeading(), new Pose2d(0.0, 0.0, new Rotation2d(0.0)));
    logger.info("Going the distance: "+desiredDistance);
  }

  private double getDistanceLeft() {
    return currentLeftDistance - initialLeftDistance;
  }

  private double getDistanceRight() {
    return currentRightDistance - initialRightDistance;
  }

  private Rotation2d getGyroHeading() {
    return Rotation2d.fromDegrees(currentHeading - initialHeading);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentLeftDistance = drive.getOdoLeft();
    currentRightDistance = drive.getOdoRight();
    currentHeading = gyro.getHeading();
    Pose2d pose = odometry.update(getGyroHeading(), getDistanceLeft(), getDistanceRight());
    Rotation2d rotation = pose.getRotation();
    Translation2d translation = pose.getTranslation();
    double rotationOutput = rotationPid.calculate(rotation.getDegrees());
    double translationOutput = translationPid.calculate(translation.getNorm());

    drive.arcadeDrive(translationOutput, rotationOutput);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.setAutonomousRunning(false);
    logger.info("Autodrive done");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return rotationPid.atSetpoint() && translationPid.atSetpoint();
  }
}
