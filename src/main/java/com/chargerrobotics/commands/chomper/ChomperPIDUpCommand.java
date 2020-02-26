/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.chomper;

import com.chargerrobotics.Constants;
import com.chargerrobotics.subsystems.ChomperSubsystem;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ChomperPIDUpCommand extends PIDCommand {
  private ChomperSubsystem chomperSubsystem;
  /**
   * Creates a new ChomperPIDUpCommand.
   */
  public ChomperPIDUpCommand(ChomperSubsystem chomperSubsystem) {
    super(
        // The controller that the command will use
        new PIDController(Constants.chomperP, Constants.chomperI, Constants.chomperD),
        // This should return the measurement
        () -> chomperSubsystem.getUpDownPosition(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          chomperSubsystem.setUpDownSpeed(output);
          // Use the output here
        });
        this.chomperSubsystem = chomperSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }

  @Override
  public void end(boolean interrupted) {
    chomperSubsystem.setUpDownSpeed(0.0);
  }
}
