package com.chargerrobotics.utils.proximitysensors;

import java.util.function.Consumer;

public class ProximityInListener implements Consumer<String> {
    private static ProximityInListener instance = null;

    public static ProximityInListener getInstance() {
        if(instance == null) {
            instance = new ProximityInListener();
            //SerialSubsystem.getInstance().registerListener("PRXYI", instance); TEMPORARILY COMMENTED OUT - Needs to be migrated later
        }
        return instance;
    }

    @Override
    public void accept(String message) {
        if(message == "increment") {
            BallCount.getInstance().incrementBallCount();
        }
    }
}