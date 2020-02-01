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
    private CANSparkMax secShooter;
    private CANPIDController shooterPIDController;
    private CANPIDController secShooterPIDController;
    private CANEncoder shooterEncoder;
    private CANEncoder secShooterEncoder;
    public double kP, kI, kD, setPoint, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
    private double prevP, prevI, prevD, prevSetPoint;
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
        secShooter = new CANSparkMax(Constants.secShooterID, MotorType.kBrushless);
        secShooterEncoder = secShooter.getEncoder();
        secShooterPIDController = secShooter.getPIDController();
        secShooterPIDController.setOutputRange(Constants.shooterMinOutput, Constants.shooterMaxOutput);
    }

    public void WSetP(double P) {
        shooterPIDController.setP(P);
        secShooterPIDController.setP(P);
    }

    public void WSetI(double I) {
        shooterPIDController.setI(I);
        secShooterPIDController.setI(I);
    }

    public void WSetD(double D) {
        shooterPIDController.setD(D);
        secShooterPIDController.setD(D);
    }

    public void WSetF(double F) {
        shooterPIDController.setFF(F);
        secShooterPIDController.setFF(F);
    }

    public void WSetSetPoint(double Point) {

    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    @Override
    public void periodic() {
        super.periodic();
        kP = SmartDashboard.getNumber("GainP", 0);
        kI = SmartDashboard.getNumber("GainI", 0);
        kD = SmartDashboard.getNumber("GainD", 0);
        setPoint = SmartDashboard.getNumber("RpmSetpoint", 0);
        if (kP != prevP) {WSetP(kP);}
        if (kI != prevI) {WSetI(kI);}
        if (kD != prevD) {WSetD(kD);}
        if (setPoint != prevSetPoint) {
            shooterPIDController.setReference(setPoint, ControlType.kVelocity);
            secShooterPIDController.setReference(-setPoint, ControlType.kVelocity);
        }
        SmartDashboard.putNumber("ShooterSpeed", shooterEncoder.getVelocity());
        prevP = kP;
        prevI = kI;
        prevD = kD;
        prevSetPoint = setPoint;
    }
}