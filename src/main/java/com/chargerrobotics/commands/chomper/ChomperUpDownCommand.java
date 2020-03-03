/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.chomper;

import com.chargerrobotics.subsystems.ChomperSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ChomperUpDownCommand extends CommandBase {
	private boolean isUp;

	/**
	 * Creates a new chomperUpDownCommand.
	 */
	public ChomperUpDownCommand(boolean isUp) {
		this.isUp = isUp;
		// Use addRequirements() here to declare subsystem dependencies.
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		if (isUp) {
			ChomperSubsystem.getInstance().setUpDownSpeed(0.5);
		} else {
			ChomperSubsystem.getInstance().setUpDownSpeed(-0.5);
		}
		SmartDashboard.putNumber("Chomper Lifter Pos", ChomperSubsystem.getInstance().chomperUpDownPosition());
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		ChomperSubsystem.getInstance().setUpDownSpeed(0.0);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}
