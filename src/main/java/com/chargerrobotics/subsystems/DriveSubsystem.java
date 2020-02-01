package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.chargerrobotics.commands.drive.ManualDriveCommand;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
    private static DriveSubsystem instance;


    private CANSparkMax leftRear;
    private CANSparkMax leftFront;
    private CANSparkMax rightRear;
    private CANSparkMax rightFront;

    private DifferentialDrive differentialDrive;
    private SpeedControllerGroup leftDriveGroup;
    private SpeedControllerGroup rightDriveGroup;

    private double leftThrottle;
    private double rightThrottle;

    private boolean brake;

    public static DriveSubsystem getInstance() {
        if(instance == null)
            instance = new DriveSubsystem();
        return instance;
    }

    public DriveSubsystem() {
        leftRear = new CANSparkMax(Constants.leftRearDrive, MotorType.kBrushless);
        leftRear.setIdleMode(IdleMode.kCoast);
        leftFront = new CANSparkMax(Constants.leftFrontDrive, MotorType.kBrushless);
        leftFront.setIdleMode(IdleMode.kCoast);
        rightRear = new CANSparkMax(Constants.rightRearDrive, MotorType.kBrushless);
        rightRear.setIdleMode(IdleMode.kCoast);
        rightFront = new CANSparkMax(Constants.rightFrontDrive, MotorType.kBrushless);
        rightFront.setIdleMode(IdleMode.kCoast);
        leftRear.setSmartCurrentLimit(40);
        leftFront.setSmartCurrentLimit(40);
        rightRear.setSmartCurrentLimit(40);
        rightFront.setSmartCurrentLimit(40);

        leftDriveGroup = new SpeedControllerGroup(leftFront, leftRear);
        leftDriveGroup.setInverted(true);
        rightDriveGroup = new SpeedControllerGroup(rightFront, rightRear);

        differentialDrive = new DifferentialDrive(leftDriveGroup, rightDriveGroup);
        differentialDrive.setDeadband(0.0);
    }

    public void setThrottle(double left, double right) {
        leftThrottle = left;
        rightThrottle = right;
    }

    public void setBrake(boolean brake) {
        this.brake = brake;
        if (this.brake) {
            leftRear.setIdleMode(IdleMode.kBrake);
            leftFront.setIdleMode(IdleMode.kBrake);
            rightRear.setIdleMode(IdleMode.kBrake);
            rightFront.setIdleMode(IdleMode.kBrake);
        }
        else {
            leftRear.setIdleMode(IdleMode.kCoast);
            leftFront.setIdleMode(IdleMode.kCoast);
            rightRear.setIdleMode(IdleMode.kCoast);
            rightFront.setIdleMode(IdleMode.kCoast);
        }
    }

    public void tankDrive(double leftPower, double rightPower) {
        if(this.brake) {
            leftPower*=0.0;
            rightPower*=0.0;
            SmartDashboard.putBoolean("Brake Mode?", true);
        }
        else {
            leftPower*=0.6;
            rightPower*=0.6;
            SmartDashboard.putBoolean("Brake Mode?", false);
        }
        SmartDashboard.putNumber("TankDriveLeftPower", leftPower);
        SmartDashboard.putNumber("TankDriveRightPower", rightPower);
        differentialDrive.tankDrive(leftPower, rightPower);
    }

    @Override
    public void periodic() {
        super.periodic();
        tankDrive(leftThrottle, rightThrottle);
        SmartDashboard.putNumber("leftFrontPower", leftFront.getAppliedOutput());
        SmartDashboard.putNumber("rightFrontPower", rightFront.getAppliedOutput());
        SmartDashboard.putNumber("rightFrontCurrent", rightFront.getOutputCurrent());
        SmartDashboard.putNumber("leftFrontCurrent", leftFront.getOutputCurrent());
        System.err.println("Running in Drive Periodic");
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ManualDriveCommand(this));
    }
}