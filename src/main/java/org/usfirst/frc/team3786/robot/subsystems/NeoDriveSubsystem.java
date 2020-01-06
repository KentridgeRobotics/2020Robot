package org.usfirst.frc.team3786.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.usfirst.frc.team3786.robot.Mappings;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class NeoDriveSubsystem extends DriveSubsystem {

	private static NeoDriveSubsystem instance;

	private CANSparkMax left;
	private CANSparkMax right;

	private DifferentialDrive differentialDrive;

	public static NeoDriveSubsystem getInstance() {
		if (instance == null)
			instance = new NeoDriveSubsystem();
		return instance;
	}

	/**
	 * Pull new values from mappings
	 */
	public void setupMotors() {
		if (differentialDrive != null)
			differentialDrive.close();
		left = new CANSparkMax(Mappings.leftMotorId.getValue(), MotorType.kBrushless);
		left.setIdleMode(IdleMode.kCoast);
		right = new CANSparkMax(Mappings.leftMotorId.getValue(), MotorType.kBrushless);
		right.setIdleMode(IdleMode.kCoast);
		left.setSmartCurrentLimit(40);
		right.setSmartCurrentLimit(40);
		left.setOpenLoopRampRate(Mappings.driveRampRate.getValue());
		right.setOpenLoopRampRate(Mappings.driveRampRate.getValue());
		differentialDrive = new DifferentialDrive(left, right);
		differentialDrive.setDeadband(0);
	}

	public void setRampRate(double rate) {
		right.setOpenLoopRampRate(rate);
		left.setOpenLoopRampRate(rate);
		Mappings.driveRampRate.setValue(rate);
	}

	public void setMotorSpeeds(double leftSpeed, double rightSpeed) {
		left.set(leftSpeed);
		right.set(-rightSpeed);
	}

	public void initDefaultCommand() {
	}

	public void arcadeDrive(double speed, double turnRate) {
		differentialDrive.arcadeDrive(-speed, turnRate);
	}

	public void tankDrive(double leftPower, double rightPower) {
		differentialDrive.tankDrive(leftPower, rightPower);
	}
}
