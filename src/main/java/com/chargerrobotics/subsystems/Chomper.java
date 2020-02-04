/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;


import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Chomper extends SubsystemBase {
  WPI_TalonSRX _talonL = new WPI_TalonSRX(1);
  WPI_TalonSRX _talonR = new WPI_TalonSRX(0);
  DifferentialDrive _drive = new DifferentialDrive(_talonL, _talonR);
  Joystick _joystick = new Joystick(0);
  /**
   * Creates a new Chomper.
   */
  public Chomper() {

  }

  @Override
  public void periodic() {
     /* factory default values */
     _talonL.configFactoryDefault();
     _talonR.configFactoryDefault();
 
     /* flip values so robot moves forward when stick-forward/LEDs-green */
     _talonL.setInverted(false); // <<<<<< Adjust this
     _talonR.setInverted(true); // <<<<<< Adjust this
 
     /*
      * WPI drivetrain classes defaultly assume left and right are opposite. call
      * this so we can apply + to both sides when moving forward. DO NOT CHANGE
      */
     _drive.setRightSideInverted(false);
    // This method will be called once per scheduler run
  }
}
