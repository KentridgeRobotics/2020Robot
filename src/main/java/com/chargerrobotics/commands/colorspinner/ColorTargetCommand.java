/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.colorspinner;

import com.chargerrobotics.Robot;
import com.chargerrobotics.Robot.ColorWheelColor;
import com.chargerrobotics.sensors.ColorSensorSerial;
import com.chargerrobotics.subsystems.ColorSpinnerSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ColorTargetCommand extends CommandBase {

	private final ColorSpinnerSubsystem colorSpinnerSubsystem;
	private final ColorSensorSerial colorSensor;
	private ColorWheelColor target;

	/**
	 * Creates a new ColorSpinnerCommand.
	 */
	public ColorTargetCommand(ColorSpinnerSubsystem colorSpinnerSubsystem, ColorSensorSerial colorSensor) {
		this.colorSpinnerSubsystem = colorSpinnerSubsystem;
		this.colorSensor = colorSensor;
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		target = Robot.getTargetColorWheelColor();
		if (target != null) colorSpinnerSubsystem.setRunning(true);
		else this.cancel();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {	
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(final boolean interrupted) {
		colorSpinnerSubsystem.setRunning(false);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return colorSensor.getColor() == target;
	}
}
