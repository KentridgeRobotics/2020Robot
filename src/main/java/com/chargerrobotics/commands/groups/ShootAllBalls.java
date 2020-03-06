/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.commands.groups;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import com.chargerrobotics.commands.groups.ReadyShooter;
import com.chargerrobotics.commands.groups.VisionDrive;
import com.chargerrobotics.commands.shooter.HoodPresetAngleCommand;
import com.chargerrobotics.commands.shooter.KickAllBalls;
import com.chargerrobotics.subsystems.LimelightSubsystem;
import com.chargerrobotics.subsystems.DriveSubsystem;
import com.chargerrobotics.Constants;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ShootAllBalls extends SequentialCommandGroup {
  /**
   * Creates a new ShootAllBalls.
   */
  public ShootAllBalls(ReadyShooter shooter, VisionDrive drive, HoodPresetAngleCommand hood, KickAllBalls kick) {
    addCommands(
      // Ready the shooter (turn on shooter, turn towards target, turn on feeder, calibrate hood) - mock:
      shooter,

      // Move to preset distance robot is set for - mock:
      drive,

      // Set hood angle to preset - mock:
      hood,

      // Kick all the balls to shooter - mock:
      kick
    );
  }
}
