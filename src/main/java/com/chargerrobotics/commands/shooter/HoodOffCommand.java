package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.ShooterHoodSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodOffCommand extends CommandBase {
    private final ShooterHoodSubsystem shooterHoodSubsystem;

    public HoodOffCommand(ShooterHoodSubsystem shooterHoodSubsystem) {
        this.shooterHoodSubsystem = shooterHoodSubsystem;
    }

    @Override
    public void initialize() {
        shooterHoodSubsystem.setRunning(false);
        SmartDashboard.putString("Hey", "HoodCommand is off");
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}