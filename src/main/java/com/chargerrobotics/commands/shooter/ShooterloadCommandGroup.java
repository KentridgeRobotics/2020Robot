package com.chargerrobotics.commands.shooter;


import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


public class ShooterloadCommandGroup extends SequentialCommandGroup {
//if and then to make sure that if you fire it and if out than say so in the code but if not than keep on firing
public ComplexAuto(DriveSubsystem drive, HatchSubsystem hatch) {
    addCommands(new DriveDistance(AutoConstants.kAutoDriveDistanceInches, AutoConstants.kAutoDriveSpeed,
    drive),
    addCommands(new Degreee(AutoConstants.distance, AutoConstants.kAutoDegree,
    drive),
    //To say how to aim it in a far away place to fire and add another thing for going backwards for 5 
    //second add is for distance to reold or go to the line with the sensor to sens balls

}

