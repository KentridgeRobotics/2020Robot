package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem instance;
    private CANSparkMax shooter;
    private CANPIDController shooterPIDController;
    private CANEncoder shooterEncoder;
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
    private boolean isRunning;


    public static ShooterSubsystem getInstance() {
        if(instance == null)
            instance = new ShooterSubsystem();
        return instance;
    }

    public ShooterSubsystem() {
        shooter = new CANSparkMax(Constants.shooterID, MotorType.kBrushless);
        shooterEncoder = shooter.getEncoder();
        shooterPIDController = shooter.getPIDController();
        shooterPIDController.setOutputRange(Constants.shooterMinOutput, Constants.shooterMaxOutput);
        shooter.setOpenLoopRampRate(3);
    }

    public void WSetP(double P) {
        shooterPIDController.setP(P);
    }

    public void WSetI(double I) {
        shooterPIDController.setI(I);
    }

    public void WSetD(double D) {
        shooterPIDController.setD(D);
    }

    public void WSetF(double F) {
        shooterPIDController.setFF(F);
    }

    public void WSetSetPoint(double Point) {

    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putNumber("ShooterSpeed", shooterEncoder.getVelocity());
        shooterPIDController.setP(SmartDashboard.getNumber("GainP", 0));
        shooterPIDController.setI(SmartDashboard.getNumber("GainI", 0));
        shooterPIDController.setD(SmartDashboard.getNumber("GainD", 0));
        shooterPIDController.setFF(SmartDashboard.getNumber("GainF", 0));
        shooterPIDController.setReference(SmartDashboard.getNumber("RpmSetpoint", 0), ControlType.kVelocity);
    }

}