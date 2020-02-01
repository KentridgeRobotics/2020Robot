package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.chargerrobotics.utils.NetworkMapping;
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
    public double kIz, kMaxOutput, kMinOutput, maxRPM;
    public final NetworkMapping<Integer> kP = new NetworkMapping<Integer>("shooter_p", 0, val -> {WSetP(val);});
    public final NetworkMapping<Integer> kI = new NetworkMapping<Integer>("shooter_i", 0, val -> {WSetI(val);});
    public final NetworkMapping<Integer> kD = new NetworkMapping<Integer>("shooter_d", 0, val -> {WSetD(val);});
    public final NetworkMapping<Integer> kF = new NetworkMapping<Integer>("shooter_f", 0, val -> {WSetF(val);});
    public final NetworkMapping<Integer> kSetPoint = new NetworkMapping<Integer>("shooter_rpm_setpoint", 0, val -> {WSetSetPoint(val);});
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

    public void WSetSetPoint(double setPoint) {
    	shooterPIDController.setReference(setPoint, ControlType.kVelocity);
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putNumber("ShooterSpeed", shooterEncoder.getVelocity());
    }

}