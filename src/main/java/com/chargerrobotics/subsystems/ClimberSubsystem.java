/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
	/**
	 * Creates a new ClimberSubsystem.
	 */

  private static ClimberSubsystem instance;
  private DoubleSolenoid hookSolenoid;

	public static ClimberSubsystem getInstance() {
		if (instance == null)
			instance = new ClimberSubsystem();
		return instance;
	}

  public ClimberSubsystem() {
    hookSolenoid = new DoubleSolenoid(2, 0, 1);
  }

  public void setUp() {
    hookSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  public void setDown() {
    hookSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  public void setStop() {
    hookSolenoid.set(DoubleSolenoid.Value.kOff);
  }

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}
