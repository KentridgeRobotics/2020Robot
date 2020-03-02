package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.chargerrobotics.utils.NetworkMapping;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterHoodSubsystem extends SubsystemBase {
    private static ShooterHoodSubsystem instance;
    private WPI_TalonSRX shooterHood;
    private DigitalInput shooterLimitSwitch;

    public final NetworkMapping<Double> kP = new NetworkMapping<Double>("hood_p", Constants.hoodP, val -> {setPIDP(val);});
    public final NetworkMapping<Double> kI = new NetworkMapping<Double>("hood_i", Constants.hoodI, val -> {setPIDI(val);});
    public final NetworkMapping<Double> kD = new NetworkMapping<Double>("hood_d", Constants.hoodD, val -> {setPIDD(val);});
    public final NetworkMapping<Double> kSetPoint = new NetworkMapping<Double>("hood_pos_setpoint", 0.0, val -> {setPIDTarget(val);});

    private boolean isRunning;

    public static ShooterHoodSubsystem getInstance() {
        if (instance == null) {
            instance = new ShooterHoodSubsystem();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
        return instance;
    }

    public ShooterHoodSubsystem() {
        shooterHood = new WPI_TalonSRX(Constants.shooterHood);
        shooterHood.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, Constants.hoodPIDLoopId, Constants.hoodTimeOutMs);
        shooterHood.setNeutralMode(NeutralMode.Brake);
        shooterHood.configPeakCurrentLimit(40);
        shooterHood.configContinuousCurrentLimit(30);
        
		setPIDP(kP.getValue());
		setPIDI(kI.getValue());
        setPIDD(kD.getValue());

        shooterLimitSwitch = new DigitalInput(1);
        
    }

    public void resetShooterEncoder() {
        shooterHood.setSelectedSensorPosition(0, Constants.hoodPIDLoopId, Constants.hoodTimeOutMs);
    }

	private void setPIDP(double P) {
		shooterHood.config_kP(Constants.hoodPIDLoopId, P, Constants.hoodTimeOutMs);
	}

	private void setPIDI(double I) {
		shooterHood.config_kI(Constants.hoodPIDLoopId, I, Constants.hoodTimeOutMs);
	}

	private void setPIDD(double D) {
		shooterHood.config_kD(Constants.hoodPIDLoopId, D, Constants.hoodTimeOutMs);
	}

	private void setPIDTarget(double setPoint) {
		shooterHood.set(ControlMode.Position, setPoint);
    }
    
    public void setHoodSpeed(double speed) {
        shooterHood.set(speed);
    }

    public boolean isLimitSwitchTriggered() {
        return !shooterLimitSwitch.get();
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
        if (isRunning) {
            setPIDTarget(kSetPoint.getValue());
            setPIDP(kP.getValue());
            setPIDI(kI.getValue());
            setPIDD(kD.getValue());
    
        }
        else
            shooterHood.set(0.0);
    }

    @Override
    public void periodic() {
        super.periodic();
        if (shooterHood.get() > 0 && isLimitSwitchTriggered()) {
            shooterHood.set(0);
            resetShooterEncoder();
        }
        SmartDashboard.putNumber("hoodCurrPos", shooterHood.getSensorCollection().getQuadraturePosition());
        SmartDashboard.putNumber("hood Current", shooterHood.getSupplyCurrent());
        SmartDashboard.putBoolean("HoodTriggered?", isLimitSwitchTriggered());
    }

}