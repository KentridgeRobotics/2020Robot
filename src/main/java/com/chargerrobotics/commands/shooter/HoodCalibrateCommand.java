package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.ShooterHoodSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodCalibrateCommand extends CommandBase {
    private final ShooterHoodSubsystem shooterHoodSubsystem;

    public HoodCalibrateCommand(ShooterHoodSubsystem shooterHoodSubsystem) {
        this.shooterHoodSubsystem = shooterHoodSubsystem;
    }

    @Override
    public void initialize() {
        shooterHoodSubsystem.setHoodSpeed(0.25);
    }

    @Override
    public void execute() {
    }
    
    @Override
    public boolean isFinished() {
        return shooterHoodSubsystem.isLimitSwitchTriggered();
    }

    @Override
    public void end(boolean interrupted) {
        shooterHoodSubsystem.setHoodSpeed(0);
        shooterHoodSubsystem.resetShooterEncoder(); // the limit switch is zero.
    }
}