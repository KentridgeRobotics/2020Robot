package org.usfirst.frc.team3786.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team3786.robot.Mappings;

public class ShooterSubsystem extends Subsystem {
    private static ShooterSubsystem instance;

    private CANSparkMax shooter = null;
    private CANSparkMax hold = null;
    private CANSparkMax pitch = null;

    public void setupShooterMotors() {
        shooter = new CANSparkMax(Mappings.shooterId.getValue(), MotorType.kBrushless);
        hold = new CANSparkMax(Mappings.holdId.getValue(), MotorType.kBrushless);
        pitch = new CANSparkMax(Mappings.pitchId.getValue(), MotorType.kBrushless);
    }

    public static ShooterSubsystem getInstance() {
        if (instance == null) {
            instance = new ShooterSubsystem();
        }
        return instance;
    }
    
    protected void initDefaultCommand() {
        
    }
}