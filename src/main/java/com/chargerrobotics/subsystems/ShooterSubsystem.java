package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.chargerrobotics.utils.NetworkMapping;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ShooterSubsystem extends SubsystemBase {
    private static ShooterSubsystem instance;
    private CANSparkMax shooter1;
    private CANSparkMax shooter2;
    private CANPIDController shooterPIDController;
    public double kIz, kMaxOutput, kMinOutput, maxRPM;
    public final NetworkMapping<Double> kP = new NetworkMapping<Double>("shooter_p", Constants.shooterkP, val -> {setPIDP(val);});
    public final NetworkMapping<Double> kI = new NetworkMapping<Double>("shooter_i", Constants.shooterkI, val -> {setPIDI(val);});
    public final NetworkMapping<Double> kD = new NetworkMapping<Double>("shooter_d", Constants.shooterkD, val -> {setPIDD(val);});
    public final NetworkMapping<Double> kF = new NetworkMapping<Double>("shooter_f", Constants.shooterFeedForward, val -> {setPIDF(val);});
    public final NetworkMapping<Double> kSetPoint = new NetworkMapping<Double>("shooter_rpm_setpoint", Constants.shooterTargetRPM, val -> {setSetPoint(val);});
    private boolean isRunning;


    public static ShooterSubsystem getInstance() {
        if(instance == null)
            instance = new ShooterSubsystem();
        return instance;
    }

    public ShooterSubsystem() {
        shooter1 = new CANSparkMax(Constants.shooterID1, MotorType.kBrushless);
        shooter2 = new CANSparkMax(Constants.shooterID2, MotorType.kBrushless);
        shooter2.follow(shooter1, true);
        shooterPIDController = shooter1.getPIDController();
        shooterPIDController.setOutputRange(Constants.shooterMinOutput, Constants.shooterMaxOutput);
        setPIDP(kP.getValue());
        setPIDI(kI.getValue());
        setPIDD(kD.getValue());
        setPIDF(kF.getValue());
        setSetPoint(kSetPoint.getValue());
    }

    private void setPIDP(double P) {
        shooterPIDController.setP(P);
    }

    private void setPIDI(double I) {
        shooterPIDController.setI(I);
    }

    private void setPIDD(double D) {
        shooterPIDController.setD(D);
    }

    private void setPIDF(double F) {
        shooterPIDController.setFF(F);
    }

    private void setPIDTarget(double setPoint) {
    	shooterPIDController.setReference(setPoint, ControlType.kVelocity);
    }

    public void setSetPoint(double val) {
        if (isRunning)
            setPIDTarget(val);
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
        setPIDTarget(isRunning ? kSetPoint.getValue() : 0);
    }

    public double getAverageVelocity() {
        return (shooter1.getEncoder().getVelocity() - shooter2.getEncoder().getVelocity()) / 2;
    }

    @Override
    public void periodic() {
        super.periodic();
        SmartDashboard.putNumber("ShooterSpeed", getAverageVelocity());
    }

}