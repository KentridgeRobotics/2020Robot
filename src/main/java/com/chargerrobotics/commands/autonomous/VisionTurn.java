/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.autonomous;

import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.subsystems.LimelightSubsystem;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class VisionTurn extends PIDCommand {
  private LimelightSubsystem limelight;

  /**
   * Creates a new VisionTurn.
   */
  public VisionTurn(final LimelightSubsystem limelightSubsystem, final DriveSubsystem driveSubsystem) {
    super(
        // The controller that the command will use
        new PIDController(0.1, 0, 0),
        // This should return the measurement
        () -> limelightSubsystem.getX(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          System.out.println("output: "+output);
          //driveSubsystem.arcadeDrive(0, output);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    limelight = limelightSubsystem;
  }

  @Override
  public void execute() {
    // TODO Auto-generated method stub
    System.out.print("Error: " + this.getController().getPositionError());
    System.out.println("\t"+"X: "+limelight.getX());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
