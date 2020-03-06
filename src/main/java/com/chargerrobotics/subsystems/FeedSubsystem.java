package com.chargerrobotics.subsystems;

import com.chargerrobotics.Constants;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class FeedSubsystem extends SubsystemBase {
    private static FeedSubsystem instance;
    
    private WPI_TalonSRX feed;

    public static FeedSubsystem getInstance() {
        if(instance == null) {
            instance = new FeedSubsystem();
            CommandScheduler.getInstance().registerSubsystem(instance);
        }
        return instance;
    }

    public FeedSubsystem() {
        feed = new WPI_TalonSRX(Constants.feedStage);
        feed.setNeutralMode(NeutralMode.Brake);
        feed.setSafetyEnabled(false);
    }

    public void setFeedRunning(boolean isRunning) {
        feed.set(isRunning ? -0.2 : 0.0);
    }

    @Override
    public void periodic() {
        
    }
}