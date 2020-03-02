package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.ShooterHoodSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodCalibrateCommand extends CommandBase {
    private final ShooterHoodSubsystem shooterHoodSubsystem;
    boolean isDone;

    public HoodCalibrateCommand(ShooterHoodSubsystem shooterHoodSubsystem) {
        this.shooterHoodSubsystem = shooterHoodSubsystem;
    }

    @Override
    public void initialize() {
        isDone = false;
        shooterHoodSubsystem.setHoodSpeed(0.1);
    }

    @Override
    public void execute() {
        if(shooterHoodSubsystem.isLimitSwitchTriggered()) {
            shooterHoodSubsystem.setHoodSpeed(0.0);
            isDone = true;
        }
    }
    
    @Override
    public boolean isFinished() {
        return isDone;
    }

    @Override
    public void end(boolean interrupted) {
        shooterHoodSubsystem.resetShooterEncoder();
    }
}