package com.chargerrobotics.commands.drive;

import com.chargerrobotics.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SlowCommand extends CommandBase {
    private DriveSubsystem driveSubsystem;

    public SlowCommand(DriveSubsystem driveSubsystem) {
        this.driveSubsystem = driveSubsystem;
    }

    @Override
    public void initialize() {
        super.initialize();
        driveSubsystem.setSlow(true);
        SmartDashboard.putBoolean("In slow mode", true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        driveSubsystem.setSlow(false);
        SmartDashboard.putBoolean("In slow mode", false);
    }
}