/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
	/**
	 * Creates a new ClimberSubsystem.
	 */

	private static ClimberSubsystem instance;
	private WPI_TalonFX climbPush1;
	private WPI_TalonFX climbPush2;
	private WPI_TalonSRX climbPull;

	public static ClimberSubsystem getInstance() {
		if(instance == null) {
			instance = new ClimberSubsystem();
			CommandScheduler.getInstance().registerSubsystem(instance);
		}
		return instance;
	}

	public ClimberSubsystem() {
		climbPush1 = new WPI_TalonFX(Constants.climbExtender);
		climbPush2 = new WPI_TalonFX(Constants.climbPush2);
		climbPull = new WPI_TalonSRX(Constants.climbPull);
		climbPush1.setSafetyEnabled(false);
		climbPush2.setSafetyEnabled(false);
		climbPull.setSafetyEnabled(false);
		climbPush1.setNeutralMode(NeutralMode.Brake);
		climbPush2.setNeutralMode(NeutralMode.Brake);
		climbPull.setNeutralMode(NeutralMode.Brake);
	}

	public void setUp() {
		climbPush1.set(0.4);
		climbPush2.set(-0.4);
		climbPull.set(0.6);
	}

	public void setDown() {
		climbPush1.set(-0.4);
		climbPush2.set(0.4);
		climbPull.set(0);
	}

	public void setStop() {
		climbPush1.set(0);
		climbPush2.set(0);
		climbPull.set(0);
	}
	
	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}
