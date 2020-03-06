package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.chargerrobotics.utils.NetworkMapping;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem instance;
    private CANSparkMax shooter1;
	private CANSparkMax shooter2;
	/*
	* P = 0.00075
	* I = 0.0000003
	* D = 0.00008
	* F = 0.0


	Later attempt with fresh battery, got this far...
	P = 0.00048
	I = .00000001
	D = 0
	*/


	// P: 0.001
	// I: 0.0000005
	// D: 0.012
    private CANPIDController shooterPIDController1;
    private CANPIDController shooterPIDController2;
    public double kIz, kMaxOutput, kMinOutput, maxRPM;
    public final NetworkMapping<Double> kP = new NetworkMapping<Double>("shooter_p", Constants.shooterP, val -> {setPIDP(val);});
    public final NetworkMapping<Double> kI = new NetworkMapping<Double>("shooter_i", Constants.shooterI, val -> {setPIDI(val);});
    public final NetworkMapping<Double> kD = new NetworkMapping<Double>("shooter_d", Constants.shooterD, val -> {setPIDD(val);});
    public final NetworkMapping<Double> kF = new NetworkMapping<Double>("shooter_f", Constants.shooterFeedForward, val -> {setPIDF(val);});
    public final NetworkMapping<Double> kSetPoint = new NetworkMapping<Double>("shooter_rpm_setpoint", Constants.shooterTargetRPM, val -> {setSetPoint(val);});
    private boolean isRunning;

	public static ShooterSubsystem getInstance() {
		if (instance == null) {
			instance = new ShooterSubsystem();
			CommandScheduler.getInstance().registerSubsystem(instance);
		}
		return instance;
	}

	public ShooterSubsystem() {
		shooter1 = new CANSparkMax(Constants.shooterLeft, MotorType.kBrushless);
		shooter2 = new CANSparkMax(Constants.shooterRight, MotorType.kBrushless);
		shooter1.setSmartCurrentLimit(Constants.shooterCurrentLimit);
		shooter2.setSmartCurrentLimit(Constants.shooterCurrentLimit);
		shooterPIDController1 = shooter1.getPIDController();
		shooterPIDController2 = shooter2.getPIDController();
		shooter1.setInverted(true);
		shooter2.setInverted(false);
		shooter1.setIdleMode(IdleMode.kCoast);
		shooter2.setIdleMode(IdleMode.kCoast);
		shooterPIDController1.setOutputRange(Constants.shooterMinOutput, Constants.shooterMaxOutput);
		shooterPIDController2.setOutputRange(Constants.shooterMinOutput, Constants.shooterMaxOutput);
		setPIDP(kP.getValue());
		setPIDI(kI.getValue());
		setPIDD(kD.getValue());
		setPIDF(kF.getValue());
		setRunning(false);
	}

	private void setPIDP(double P) {
		shooterPIDController1.setP(P);
		shooterPIDController2.setP(P);
	}

	private void setPIDI(double I) {
		shooterPIDController1.setI(I);
		shooterPIDController2.setI(I);
	}

	private void setPIDD(double D) {
		shooterPIDController1.setD(D);
		shooterPIDController2.setD(D);
	}

	private void setPIDF(double F) {
		shooterPIDController1.setFF(F);
		shooterPIDController2.setFF(F);
	}

	private void setPIDTarget(double setPoint) {
		shooterPIDController1.setReference(-setPoint, ControlType.kVelocity);
		shooterPIDController2.setReference(-setPoint, ControlType.kVelocity);
	}

	public void setSetPoint(double val) {
		if (isRunning)
			setPIDTarget(val);
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		if (isRunning) {
			setPIDTarget(kSetPoint.getValue());
		}
		else {
			shooter1.set(0.0);
			shooter2.set(0.0);
		}
	}

	public double getAverageVelocity() {
		return (shooter1.getEncoder().getVelocity() + shooter2.getEncoder().getVelocity()) / 2;
	}

	@Override
	public void periodic() {
		super.periodic();
		SmartDashboard.putNumber("ShooterSpeed", getAverageVelocity());
		SmartDashboard.putNumber("shooterSpeed1", shooter1.getEncoder().getVelocity());
		SmartDashboard.putNumber("shooterSpeed2", shooter2.getEncoder().getVelocity());
		SmartDashboard.putNumber("motor1temp", shooter1.getMotorTemperature());
		SmartDashboard.putNumber("motor2temp", shooter2.getMotorTemperature());
	}

}