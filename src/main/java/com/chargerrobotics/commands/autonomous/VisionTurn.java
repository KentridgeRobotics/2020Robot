/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.autonomous;

import java.util.function.DoubleSupplier;

import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.subsystems.LimelightSubsystem;
import com.chargerrobotics.utils.NetworkMapping;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class VisionTurn extends PIDCommand {
  private static LimelightSubsystem limelight;

  private static PIDController pid;
  public final NetworkMapping<Double> kP = new NetworkMapping<Double>("vision_p", 0.1, val -> {setPIDP(val);});
  public final NetworkMapping<Double> kI = new NetworkMapping<Double>("vision_i", 0.0, val -> {setPIDI(val);});
  public final NetworkMapping<Double> kD = new NetworkMapping<Double>("vision_d", 0.0, val -> {setPIDD(val);});

  /**
   * Creates a new VisionTurn.
   */
  public VisionTurn(final LimelightSubsystem limelightSubsystem, final DriveSubsystem driveSubsystem) {
    
    super(
        // The controller that the command will use
        setPID(new PIDController(0.1, 0, 0)),
        // This should return the measurement
        () -> limelightSubsystem.getX(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {driveSubsystem.setSpeeds(output * 0.1, -output * 0.1); System.out.println("Turn Target: " + output);},
        limelightSubsystem);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    limelight = limelightSubsystem;
		setPIDP(kP.getValue());
		setPIDI(kI.getValue());
		setPIDD(kD.getValue());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
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

}
