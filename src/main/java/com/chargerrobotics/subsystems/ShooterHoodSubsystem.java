package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterHoodSubsystem extends SubsystemBase {
    private static ShooterHoodSubsystem instance;
    private WPI_TalonSRX shooterHood;
    private int absolutePosition;
    private int targetPos;

    private double kP = Constants.hoodP;
    private double kI = Constants.hoodI;
    private double kD = Constants.hoodD;
    private double prevP, prevI, prevD;
    
    private int testSetpoint = 50;
    private int prevTestSetpoint;

    private boolean isRunning = false;

    public static ShooterHoodSubsystem getInstance() {
        if (instance == null) {
            instance = new ShooterHoodSubsystem();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
        return instance;
    }

    public ShooterHoodSubsystem() {
        shooterHood = new WPI_TalonSRX(Constants.shooterHoodID);
        shooterHood.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.hoodPIDLoopId, Constants.hoodTimeOutMs);
        shooterHood.setSensorPhase(Constants.hoodSensorPhase);
        shooterHood.configAllowableClosedloopError(Constants.hoodGainSlot, Constants.hoodErrorThreshold);
        setPIDP(Constants.hoodP);
        setPIDI(Constants.hoodI);
        setPIDD(Constants.hoodD);
        setPIDF(Constants.hoodF);
        absolutePosition = shooterHood.getSensorCollection().getQuadraturePosition();
        if(Constants.hoodSensorPhase) {absolutePosition *= -1;}
        if(Constants.hoodMotorInverted) {absolutePosition *= -1;}
        shooterHood.setSelectedSensorPosition(absolutePosition, Constants.hoodPIDLoopId, Constants.hoodTimeOutMs);
    }

    private void setPIDP(double p) {
        shooterHood.config_kP(Constants.hoodGainSlot, p);
    }

    private void setPIDI(double i) {
        shooterHood.config_kI(Constants.hoodGainSlot, i);
    }

    private void setPIDD(double d) {
        shooterHood.config_kD(Constants.hoodGainSlot, d);
    }

    private void setPIDF(double f) {
        shooterHood.config_kF(Constants.hoodGainSlot, f);
    }

    public void setShooterHoodSetPoint(int degrees) {
        targetPos = degrees;
        setPIDTarget();
    }

    private void setPIDTarget() {
        shooterHood.set(ControlMode.Position, targetPos);
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
        if(isRunning){
            setPIDTarget();
        }
        else {
            shooterHood.set(0.0);
        }
    }

    private void printDashboardValues() {
        SmartDashboard.putNumber("hoodP", kP);
        SmartDashboard.putNumber("hoodI", kI);
        SmartDashboard.putNumber("hoodD", kD);
        SmartDashboard.putNumber("testSetpoint", testSetpoint);
        SmartDashboard.putNumber("hoodCurrPos", shooterHood.getSensorCollection().getQuadraturePosition());
        }

    @Override
    public void periodic() {
        super.periodic();
        printDashboardValues();
        kP = SmartDashboard.getNumber("hoodP", 0);
        kI = SmartDashboard.getNumber("hoodI", 0);
        kD = SmartDashboard.getNumber("hoodD", 0);
        testSetpoint = (int) SmartDashboard.getNumber("testSetpoint", -1);
        if(kP != prevP) {setPIDP(kP);}
        if(kI != prevI) {setPIDI(kI);}
        if(kD != prevD) {setPIDD(kD);}
        if(testSetpoint != prevTestSetpoint) {setShooterHoodSetPoint(testSetpoint);
        }
        prevP = kP;
        prevI = kI;
        prevD = kD;
        prevTestSetpoint = testSetpoint;
    }

}