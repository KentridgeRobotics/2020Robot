/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import com.chargerrobotics.commands.groups.ShootAllBalls;
import com.chargerrobotics.commands.autonomous.AutoDriveLinear;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonomousShootScoot extends SequentialCommandGroup {
  /**
   * Creates a new AutonomousShootScoot.
   * 
   * Move off the line (5 pts), turn to target, shoot the balls...make 25+ points!!!
   */
  public AutonomousShootScoot(AutoDriveLinear diveLinear, ShootAllBalls shoot) {
    addCommands(
      // Turn towards the target - mock:
      // new AutoTurnStationary(..., turnStationary),

      // Shoot all the balls - mock:
      // new ShootAllBalls(..., shoot),

      // Move to a position that addresses section 4.2:  "exit the infinite vertical volume"...move backwards? - mock:
      // new AutoDriveLinear(..., driveLinear)
    );
  }
}
