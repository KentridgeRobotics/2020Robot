package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem instance;
    private CANSparkMax shooter;
    private CANPIDController shooterPIDController;
    private CANEncoder shooterEncoder;
    
    private boolean isRunning;

    public static ShooterSubsystem getInstance() {
        if(instance == null)
            instance = new ShooterSubsystem();
        return instance;
    }

    public ShooterSubsystem() {
        shooter = new CANSparkMax(Constants.shooterID, MotorType.kBrushless);
        shooterPIDController = shooter.getPIDController();
        shooterPIDController.setP(Constants.shooterkP);
        shooterPIDController.setI(Constants.shooterkI);
        shooterPIDController.setP(Constants.shooterkD);
        shooterPIDController.setFF(Constants.shooterFeedForward);
        shooterPIDController.setOutputRange(Constants.shooterMinOutput, Constants.shooterMaxOutput);
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public void periodic() {
        super.periodic();
        shooterPIDController.setReference(isRunning ? Constants.shooterTargetRPM : 0.0, ControlType.kVelocity);
    }

}