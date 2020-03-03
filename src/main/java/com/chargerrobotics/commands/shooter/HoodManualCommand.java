package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.ShooterHoodSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class HoodManualCommand extends CommandBase {
    private boolean isGoingUp;
    private final ShooterHoodSubsystem shooterHoodSubsystem;
    
    public HoodManualCommand(ShooterHoodSubsystem shooterHoodSubsystem, boolean isGoingUp) {
        this.isGoingUp = isGoingUp;
        this.shooterHoodSubsystem = shooterHoodSubsystem;
    }

    @Override
    public void initialize() {
        shooterHoodSubsystem.setHoodSpeed(isGoingUp ? 0.25 : -0.25);
    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        shooterHoodSubsystem.setHoodSpeed(0);
    }

}