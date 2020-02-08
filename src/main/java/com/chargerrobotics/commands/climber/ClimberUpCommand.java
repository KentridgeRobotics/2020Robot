/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.climber;

import com.chargerrobotics.subsystems.ClimberSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClimberUpCommand extends CommandBase {

	private final ClimberSubsystem climberSubsystem;

	/**
	 * Creates a new ClimberCommand.
	 */
	public ClimberUpCommand(ClimberSubsystem climberSubsystem) {
		this.climberSubsystem = climberSubsystem;
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		climberSubsystem.setUp();
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		climberSubsystem.setStop();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
