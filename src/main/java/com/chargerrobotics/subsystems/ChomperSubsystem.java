/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ChomperSubsystem extends SubsystemBase {
  
  private static ChomperSubsystem instance;
  private DigitalInput chomperLimitSwitch = new DigitalInput(Constants.chomperLimitSwitch);
  private boolean isUpDownRunning;
  private boolean isFeedRunning;
  private WPI_TalonSRX chomperUpDown;
  private CANSparkMax chomperFeed;

  private double targetDownPos;
  private double targetUpPos;

  /**
   * Creates a new Chomper.
   */

  // periodic
  public static ChomperSubsystem getInstance() {
    if (instance == null)
      instance = new ChomperSubsystem();
      CommandScheduler.getInstance().registerSubsystem(instance);
    return instance;
  }

  public ChomperSubsystem() {
    chomperUpDown = new WPI_TalonSRX(Constants.chomperLift);
    chomperUpDown.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
    chomperFeed = new CANSparkMax(Constants.chomperFeed, MotorType.kBrushless);
  }

  public void setChomperUpDownRunning(boolean isRunning) {
    isUpDownRunning = isRunning;

  }

  public double chomperUpDownPosition() {
    return chomperUpDown.getSensorCollection().getPulseWidthPosition();
  }

  public boolean isLimitSwitchTriggered() {
    return chomperLimitSwitch.get();
  }

  public void setChomperTargetUp(double val) {
    targetUpPos = val;
  }

  public double getChomperTargetUp() {
    return targetUpPos;
  }

  public void setChomperTargetDown(double val) {
    targetDownPos = val;
  }

  public double getChomperTargetDown() {
    return targetDownPos;
  }

  public void setChomperFeedRunning(boolean isRunning) {
    isFeedRunning = isRunning;
    if(isFeedRunning) {
      setFeedSpeed(1);
    }
    else {
      setFeedSpeed(0.0);
    }
  }

  public void setUpDownSpeed(double speed) {
    chomperUpDown.set(speed);
  }

  public void setFeedSpeed(double speed) {
    chomperFeed.set(speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("Chomper", "In periodic");
  }
}
