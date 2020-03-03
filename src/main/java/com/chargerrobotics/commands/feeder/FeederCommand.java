package com.chargerrobotics.commands.feeder;

import com.chargerrobotics.subsystems.FeedSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class FeederCommand extends CommandBase {
    private final FeedSubsystem feedSubsystem;

    public FeederCommand(FeedSubsystem feedSubsystem) {
        this.feedSubsystem = feedSubsystem;
    }

    @Override
    public void initialize() {
        feedSubsystem.setFeedRunning(true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        feedSubsystem.setFeedRunning(false);
    }
}