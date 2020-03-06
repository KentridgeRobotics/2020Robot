/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.groups;

import com.chargerrobotics.commands.groups.VisionTurn;
import com.chargerrobotics.commands.shooter.ShooterOnCommand;
import com.chargerrobotics.commands.feeder.FeederCommand;
import com.chargerrobotics.commands.shooter.HoodCalibrateCommand;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ReadyShooter extends ParallelCommandGroup {
  /**
   * Creates a new ReadyShooter.
   * 
   * Get things ready to shoot.
   */
  public ReadyShooter(ShooterOnCommand shooterOn, VisionTurn visionTurn, FeederCommand feeder, HoodCalibrateCommand hoodCalibrate) {
    addCommands(
      // Align to target - mock:
      // new VisionTurn(constants..., visionTurn),

      // Turn on shooter to target RPM - mock:
      // new ShooterOnCommand(constants..., shooterOn),

      // Turn on feeder - mock:
      // new FeederCommand(..., feeder),

      // Calibrate hood - mock:
      // new HoodCalibrateCommand(..., hoodCalibrate)

    );
  }
}
