/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import com.chargerrobotics.commands.groups.ReadyShooter;
import com.chargerrobotics.commands.autonomous.AutoMoveToDistance;
import com.chargerrobotics.commands.autonomous.AutoTurnStationary;
import com.chargerrobotics.commands.shooter.HoodPresetAngleCommand;
import com.chargerrobotics.commands.autonomous.AutoKickAllBalls;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonomousShoot extends SequentialCommandGroup {
  /**
   * Creates a new AutonomousShoot.
   * 
   * Move off the line (5 pts), turn to target, shoot the balls...make 25+ points!!!
   */
  public AutonomousShoot(AutoMoveToDistance moveToDistance, AutoTurnStationary turnStationary, ReadyShooter readyShooter, HoodPresetAngleCommand hoodAngle, AutoKickAllBalls kickBalls) {
    addCommands(
      // Turn towards the target - mock:
      // new AutoTurnStationary(..., turnStationary),

      // Ready the shooter (turn on shooter, turn towards target, turn on feeder, calibrate hood) - mock:
      // new ReadyShooter(..., readyShooter),
    
      // Move back far enough to clear the start line - mock:
      // new AutoMoveToDistance(..., moveToDistance),

      // Set hood angle to preset - mock:
      // new HoodPresetAngleCommand(..., hoodAngle),

      // Kick all the balls to shooter - mock:
      // new AutoKickAllBalls(..., kickBalls)

    );
  }
}
