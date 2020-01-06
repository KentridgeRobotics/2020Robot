package org.usfirst.frc.team3786.robot.commands;

import org.usfirst.frc.team3786.robot.OI;
import org.usfirst.frc.team3786.robot.Robot;
import org.usfirst.frc.team3786.robot.utils.Gyroscope;

import edu.wpi.first.wpilibj.command.Command;

public class DriveCommand extends Command {

	public static DriveCommand instance;

	private boolean isGyroCalibrated = false;

	public static DriveCommand getInstance() {
		if (instance == null)
			instance = new DriveCommand();
		return instance;
	}

	private DriveCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (!isGyroCalibrated)
			isGyroCalibrated = Gyroscope.getInstance().isCalibrated();

		Robot.drive.tankDrive(OI.getLeftThrottle(), OI.getRightThrottle());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}
}
