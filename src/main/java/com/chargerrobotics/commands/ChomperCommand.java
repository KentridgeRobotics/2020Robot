/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands;

import com.chargerrobotics.subsystems.ChomperSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ChomperCommand extends CommandBase {

	private final ChomperSubsystem chomperSubsystem;

	/**
	 * Creates a new LimelightCommand.
	 */
	public ChomperCommand(ChomperSubsystem chomperSubsystem) {
		this.chomperSubsystem = chomperSubsystem;
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		chomperSubsystem.setRunning(true);
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		chomperSubsystem.setRunning(false);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
