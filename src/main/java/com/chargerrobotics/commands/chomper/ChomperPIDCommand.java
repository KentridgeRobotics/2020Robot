/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.chomper;

import com.chargerrobotics.subsystems.ChomperSubsystem;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ChomperPIDCommand extends PIDCommand {
  /**
   * Creates a new ChomperPIDCommand.
   */
  public ChomperPIDCommand(final double position, DutyCycleEncoder positionEcoder,ChomperSubsystem chomperSubsystem) {
    super(
        new PIDController(0.1, 0, 0),
        () -> positionEcoder.getPositionOffset(),
        () -> position,
        output -> {
          chomperSubsystem.setUpDownSpeed(output);
          // Use the output here
        });
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
