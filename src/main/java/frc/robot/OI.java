package frc.robot;

import frc.robot.utils.XboxController;

public class OI {
    private static XboxController primaryController = null;
    private static XboxController secondaryController = null;

    public static XboxController getPrimaryController() {
        if (primaryController == null)
            primaryController = new XboxController(Mappings.primaryController);
        return primaryController;
    }

    public static XboxController getSecondaryController() {
        if (secondaryController == null)
            secondaryController = new XboxController(Mappings.secondaryController);
        return secondaryController;
    }

    public static double getRightThrottle() { //We're doing tank drive to start
        return primaryController.getRightStickY();
    }

    public static double getLeftThrottle() { //We're doing tank drive to start
        return primaryController.getLeftStickY();
    }
}