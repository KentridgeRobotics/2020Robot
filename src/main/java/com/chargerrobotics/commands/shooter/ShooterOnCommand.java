package com.chargerrobotics.commands.shooter;

import com.chargerrobotics.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShooterOnCommand extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final ShooterSubsystem shooterSubsystem;

    public ShooterOnCommand (ShooterSubsystem shootersubsystem) {
        this.shooterSubsystem = shootersubsystem;
    }

    @Override
    public void initialize() {
        super.initialize();
        shooterSubsystem.setRunning(true);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}