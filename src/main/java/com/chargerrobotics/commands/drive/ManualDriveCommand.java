package com.chargerrobotics.commands.drive;

import com.chargerrobotics.RobotContainer;
import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.utils.XboxController;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ManualDriveCommand extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final DriveSubsystem driveSubsystem;
    private XboxController primary = RobotContainer.getPrimary();

    public ManualDriveCommand (DriveSubsystem driveSubsystem) {
        this.driveSubsystem = driveSubsystem;
    }
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        driveSubsystem.tankDrive(primary.getLeftStickY(), primary.getRightStickY());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}