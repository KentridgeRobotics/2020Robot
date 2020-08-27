/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.groups;

import java.util.function.DoubleSupplier;

import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.subsystems.LimelightSubsystem;
import com.chargerrobotics.utils.NetworkMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class VisionTurn extends PIDCommand {
  private static LimelightSubsystem limelight;
  private static final Logger logger = LoggerFactory.getLogger(VisionTurn.class);

  private static PIDController pid;
  private long startTime;
  private static final long delay = 250; // wait 250 ms for limelight to lock on
  public final NetworkMapping<Double> kP = new NetworkMapping<Double>("vision_p", 0.015, val -> {
    setPIDP(val);
  });
  public final NetworkMapping<Double> kI = new NetworkMapping<Double>("vision_i", 0.0, val -> {
    setPIDI(val);
  });
  public final NetworkMapping<Double> kD = new NetworkMapping<Double>("vision_d", 0.0001, val -> {
    setPIDD(val);
  });

  /**
   * Creates a new VisionTurn.
   */
  public VisionTurn(final LimelightSubsystem limelightSubsystem, final DriveSubsystem driveSubsystem) {

    super(
        // The controller that the command will use
        setPID(new PIDController(0.015, 0.0, 0.0001)),
        // This should return the measurement
        () -> limelightSubsystem.getX(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output to move the robot
        output -> {
          driveSubsystem.setSpeeds(output, -output);
          logger.info("Turn Target - Left: " + output + " Right: " + -output + " Distance: "
              + limelightSubsystem.distance() + " inches");
        }, limelightSubsystem);

    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    limelight = limelightSubsystem;
    setPIDP(kP.getValue());
    setPIDI(kI.getValue());
    setPIDD(kD.getValue());
  }


  private static PIDController setPID(PIDController pid) {
    VisionTurn.pid = pid;
    return pid;
  }

  protected static double getPIDInput() {
    return limelight.getX();
  }

  private void setPIDP(double P) {
    pid.setP(P);
  }

  private void setPIDI(double I) {
    pid.setI(I);
  }

  private void setPIDD(double D) {
    pid.setD(D);
  }

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    super.initialize();
    limelight.setLEDStatus(true);
    startTime = System.currentTimeMillis();
  }

  @Override
  public void end(boolean interrupted) {
    // TODO Auto-generated method stub
    super.end(interrupted);
    limelight.setLEDStatus(false);
  }

  @Override
  public void execute() {
    long now = System.currentTimeMillis();
    if (now - startTime > delay) {
      super.execute();
      SmartDashboard.putNumber("VisionError", getController().getPositionError());
    }
  }
}
