package com.chargerrobotics.commands.chomper;

import com.chargerrobotics.Constants;
import com.chargerrobotics.subsystems.ChomperSubsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ChomperCalibrateCommand extends CommandBase {
    private final ChomperSubsystem chomperSubsystem;
    private double encoderRef;
    private boolean isDone;
    private static final Logger logger = LoggerFactory.getLogger(ChomperCalibrateCommand.class);

    public ChomperCalibrateCommand(ChomperSubsystem chomperSubsystem) {
        this.chomperSubsystem = chomperSubsystem;
    }

    @Override
    public void initialize() {
        isDone = false;
        chomperSubsystem.setUpDownSpeed(-0.3);
        logger.info("Calibrating chomper");
    }

    @Override
    public void execute() {
        if(chomperSubsystem.isLimitSwitchTriggered()) {
            encoderRef = chomperSubsystem.chomperUpDownPosition();
            isDone = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isDone;
    }

    @Override
    public void end(boolean interrupted) {
        chomperSubsystem.setUpDownSpeed(0);
        double targetDown = encoderRef - Constants.chomperDistToDown;
        double targetUp = targetDown + Constants.chomperDistBottomToUp;
        chomperSubsystem.setChomperTargetUpDown(targetUp, targetDown);
        logger.info("Chomper is calibrated!");
    }
}
