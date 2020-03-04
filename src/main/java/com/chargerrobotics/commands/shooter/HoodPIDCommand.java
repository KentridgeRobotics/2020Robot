/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.ShooterHoodSubsystem;
import com.chargerrobotics.utils.NetworkMapping;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class HoodPIDCommand extends PIDCommand {
  private ShooterHoodSubsystem shooterHoodSubsystem;
  private static PIDController pid;
  private static int setPoint;

  public static final NetworkMapping<Double> kP = new NetworkMapping<Double>("hood_p", 0.0, val -> {
    setPIDP(val);
  });

  public static final NetworkMapping<Double> kI = new NetworkMapping<Double>("hood_i", 0.0, val -> {
    setPIDP(val);
  });

  public static final NetworkMapping<Double> kD = new NetworkMapping<Double>("hood_d", 0.0, val -> {
    setPIDP(val);
  });

  public static final NetworkMapping<Integer> hood_setPoint = new NetworkMapping<Integer>("hood_SetPointDegrees", 50, val -> {
    setPoint = val;
  });

  /**
   * Creates a new HoodPIDCommand.
   */
  public HoodPIDCommand(ShooterHoodSubsystem shooterHoodSubsystem) {
    super(
        // The controller that the command will use
        setPID(new PIDController(kP.getValue(), kI.getValue(), kD.getValue())),
        // This should return the measurement
        () -> shooterHoodSubsystem.getHoodPosition(),
        // This should return the setpoint (can also be a constant)
        () -> shooterHoodSubsystem.findHoodTargetTicks(setPoint),
        // This uses the output
        output -> {
          shooterHoodSubsystem.setHoodSpeed(-output);
          SmartDashboard.putNumber("hoodOutput", output);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    this.shooterHoodSubsystem = shooterHoodSubsystem;
    setPIDP(kP.getValue());
    setPIDI(kI.getValue());
    setPIDD(kD.getValue());
    getController().setSetpoint(shooterHoodSubsystem.findHoodTargetTicks(setPoint));
    getController().setTolerance(0);
  }

  private static PIDController setPID(PIDController pid) {
     HoodPIDCommand.pid = pid;
    return pid;
  }

  private static void setPIDP(double p) {
    pid.setP(p);
  }

  private static void setPIDI(double i) {
    pid.setI(i);
  }

  private static void setPIDD(double d) {
    pid.setD(d);
  }

  @Override
  public void initialize() {
    SmartDashboard.putBoolean("HoodPIDCommandRunning", true);
  }

  @Override
  public void execute() {
    super.execute();
    SmartDashboard.putNumber("hoodError", getController().getPositionError());
    SmartDashboard.putNumber("hoodSetpointTicks", shooterHoodSubsystem.findHoodTargetTicks(setPoint));
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    shooterHoodSubsystem.setHoodSpeed(0);
    SmartDashboard.putBoolean("HoodPIDCommandRunning", false);
  }
}
