package org.usfirst.frc.team3786.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class DriveSubsystem extends Subsystem {

	public DriveSubsystem() {
		this.setupMotors();
	}

	/**
	 * Pull new values from mappings
	 */
	public abstract void setupMotors();

	/**
	 * Sets the motor ramp rate
	 * 
	 * @param rate Ramp rate
	 */
	public abstract void setRampRate(double rate);

	/**
	 * Individually sets motor speeds Primarily used for autonomous control
	 * 
	 * @param leftSpeed  Left motor speed
	 * @param rightSpeed Right motor speed
	 */
	public abstract void setMotorSpeeds(double leftSpeed, double rightSpeed);

	/**
	 * Arcade drive
	 * 
	 * @param speed    Driving speed
	 * @param turnRate Turning rate
	 */
	public abstract void arcadeDrive(double speed, double turnRate);

	/**
	 * Tank drive
	 * 
	 * @param leftSpeed  Left speed
	 * @param rightSpeed Right speed
	 */
	public abstract void tankDrive(double leftSpeed, double rightSpeed);

}
