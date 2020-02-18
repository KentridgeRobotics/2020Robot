package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.ShooterHoodSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodOnCommand extends CommandBase {
    private final ShooterHoodSubsystem shooterHoodSubsystem;

    public HoodOnCommand(ShooterHoodSubsystem shooterHoodSubsystem) {
        this.shooterHoodSubsystem = shooterHoodSubsystem;
    }

    @Override
    public void initialize() {
        super.initialize();
        shooterHoodSubsystem.setRunning(true);
    }

    @Override
    public void execute() {
        SmartDashboard.putString("Hey", "HoodOnCommand is Running");
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}