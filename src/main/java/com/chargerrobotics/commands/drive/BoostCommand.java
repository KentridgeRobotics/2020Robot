package com.chargerrobotics.commands.drive;

import com.chargerrobotics.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class BoostCommand extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    
    public BoostCommand(DriveSubsystem driveSubsystem) {
        this.driveSubsystem = driveSubsystem;
    }

    @Override
    public void initialize() {
        super.initialize();
        driveSubsystem.setBoost(true);
        SmartDashboard.putBoolean("In boost mode", true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        driveSubsystem.setBoost(false);
        SmartDashboard.putBoolean("In boost mode", false);
    }
}