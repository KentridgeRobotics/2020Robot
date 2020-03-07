package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.chargerrobotics.utils.NetworkMapping;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
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

    public static ShooterHoodSubsystem getInstance() {
        if (instance == null) {
            instance = new ShooterHoodSubsystem();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
        return instance;
    }

    public ShooterHoodSubsystem() {
        shooterHood = new WPI_TalonSRX(Constants.shooterHood);
        shooterHood.setSafetyEnabled(false);
        shooterHood.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.QuadEncoder, Constants.hoodPIDLoopId, Constants.hoodTimeOutMs);
        shooterHood.setNeutralMode(NeutralMode.Brake);
        shooterHood.configPeakCurrentLimit(40);
        shooterHood.configContinuousCurrentLimit(30);
        shooterHood.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
    }

    public void resetShooterEncoder() {
        shooterHood.setSelectedSensorPosition(0, Constants.hoodPIDLoopId, Constants.hoodTimeOutMs);
    }
    
    public void setHoodSpeed(double speed) {
        shooterHood.set(speed);
    }

    public double findHoodTargetTicks(double targetHoodAngDegrees) {
        double outPut = -34.217*targetHoodAngDegrees + 2532.087;
        return outPut;
    }

    public boolean isLimitSwitchTriggered() {
    	return shooterHood.getSensorCollection().isFwdLimitSwitchClosed();
        //return !shooterLimitSwitch.get();
    }

    public double getHoodPosition() {
        return (double)shooterHood.getSensorCollection().getQuadraturePosition();
    }

    @Override
    public void periodic() {
        super.periodic();
        if (isLimitSwitchTriggered())
            resetShooterEncoder();
        SmartDashboard.putNumber("hoodCurrPos", shooterHood.getSensorCollection().getQuadraturePosition());
        SmartDashboard.putNumber("hood Current", shooterHood.getSupplyCurrent());
        SmartDashboard.putBoolean("HoodTriggered?", isLimitSwitchTriggered());
    }

}