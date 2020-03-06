/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.chargerrobotics.utils.NetworkMapping;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class KickerSubsystem extends SubsystemBase {
  /**
   * Creates a new KickerSubsystem.
   */
  private static KickerSubsystem instance;
  private boolean isRunning;
  public double speed;
  private WPI_TalonSRX kickerMotor;

  public final NetworkMapping<Double> kP = new NetworkMapping<Double>("kicker_speed", 1.0, val -> {speed = val;});

  public static KickerSubsystem getInstance() {
    if (instance == null) {
      instance = new KickerSubsystem();
      //SmartDashboard.putNumber("KickerMotor", 0.1);
      CommandScheduler.getInstance().registerSubsystem(instance);
    }
    return instance;
  }

  public KickerSubsystem() {
    kickerMotor = new WPI_TalonSRX(Constants.kicker);
    kickerMotor.setSafetyEnabled(false);
  }

  public void setRunning(boolean isRunning) {
    this.isRunning = isRunning;
    //speed = SmartDashboard.getNumber("KickerMotor", 0.0);
    kickerMotor.set(this.isRunning ? 1.0 : 0.0);
  }

  @Override
  public void periodic() {
  }
}
