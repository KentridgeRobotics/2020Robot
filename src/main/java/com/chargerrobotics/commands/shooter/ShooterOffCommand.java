package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShooterOffCommand extends CommandBase {
	private final ShooterSubsystem shooterSubsystem;

	public ShooterOffCommand(ShooterSubsystem shooterSubsystem) {
		this.shooterSubsystem = shooterSubsystem;
	}

	@Override
	public void initialize() {
		super.initialize();
		shooterSubsystem.setRunning(false);
	}

	@Override
	public boolean isFinished() {
		return true;
	}
}