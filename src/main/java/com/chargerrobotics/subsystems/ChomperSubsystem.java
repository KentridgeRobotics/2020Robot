/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import com.ctre.phoenix.motorcontrol.can.*;



import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ChomperSubsystem extends SubsystemBase {
  
  private static ChomperSubsystem instance;
  private boolean isRunning;
  private final WPI_TalonSRX chomperMotor;

  /**
   * Creates a new Chomper.
   */

  // periodic
  public static ChomperSubsystem getInstance() {
    if (instance == null)
      instance = new ChomperSubsystem();
    return instance;
  }

  public ChomperSubsystem() {
    chomperMotor = new WPI_TalonSRX(32);
  }

  public void setRunning(final boolean isRunning) {
    this.isRunning = isRunning;

    //
  }

  public void setSpeed(final double speed) {
    if (isRunning) chomperMotor.set(speed);
  }
  @Override
  public void periodic() {
    
  }
}
