/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import com.chargerrobotics.utils.SampleSerialListener;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SampleSerialListenerSubsystem extends SubsystemBase {
  /**
   * Creates a new SampleSerialListenerSubsystem.
   */
  public SampleSerialListenerSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    Integer number = SampleSerialListener.getInstance().getLatestNumber();
    if (number != null) {
      System.err.println("This is the number we want to use: "+number);
    }
  }
}
