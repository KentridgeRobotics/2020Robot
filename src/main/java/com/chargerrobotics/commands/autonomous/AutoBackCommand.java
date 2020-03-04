package com.chargerrobotics.commands.autonomous;

import com.chargerrobotics.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoBackCommand extends CommandBase {

    // Controller IDs
    public static final long maxDriveTimeMilliseconds = 500;
    private long startTimeMilliseconds = 0;
    private DriveSubsystem drivesubsystem;

    public AutoBackCommand(DriveSubsystem drivesubsystem) {
        this.drivesubsystem = drivesubsystem;
    }

    @Override
    public void initialize() {
        startTimeMilliseconds = System.currentTimeMillis();
        drivesubsystem.setThrottle(-0.4, -0.4);
    }

    @Override
    public boolean isFinished(){
        long currentTime=System.currentTimeMillis();
        boolean isdone=currentTime>(startTimeMilliseconds+maxDriveTimeMilliseconds);
        return isdone;

    }

    @Override
    public void end(boolean interrupted) {
        // TODO Auto-generated method stub
        drivesubsystem.setThrottle(0,0);
        super.end(interrupted);
    }
}