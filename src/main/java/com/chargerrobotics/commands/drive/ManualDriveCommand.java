package com.chargerrobotics.commands.drive;

import com.chargerrobotics.RobotContainer;
import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.utils.XboxController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualDriveCommand extends CommandBase {
	private final DriveSubsystem driveSubsystem;
	private XboxController primary = RobotContainer.primary;

	public ManualDriveCommand(DriveSubsystem driveSubsystem) {
		this.driveSubsystem = driveSubsystem;
		addRequirements(driveSubsystem);
	}

	@Override
	public void initialize() {
	}

	@Override
	public void execute() {
		SmartDashboard.putNumber("leftStick", primary.getLeftStickY());
		SmartDashboard.putNumber("rightStick", primary.getRightStickY());
		driveSubsystem.setThrottle(primary.getLeftStickY(), -primary.getRightStickY());
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}