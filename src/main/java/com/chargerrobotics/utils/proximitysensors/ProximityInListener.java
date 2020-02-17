package com.chargerrobotics.utils.proximitysensors;

import java.util.function.Consumer;

import com.chargerrobotics.subsystems.SerialSubsystem;

public class ProximityInListener implements Consumer<String> {
    private static ProximityInListener instance = null;

    public static ProximityInListener getInstance() {
        if(instance == null) {
            instance = new ProximityInListener();
            SerialSubsystem.getInstance().registerListener("PRXYI", instance);
        }
        return instance;
    }

    @Override
    public void accept(String message) {
        try {
            if(message == "increment") {
                BallCount.getInstance().incrementBallCount();
            }
        }
        catch(NumberFormatException e) {
            System.err.println("Bad Message in ProximityInListener" + message);
        }
    }
}