package com.chargerrobotics.utils.proximitysensors;

import java.util.function.Consumer;

public class ProximityOutListener implements Consumer<String> {
    private static ProximityOutListener instance = null;

    public static ProximityOutListener getInstance() {
        if(instance == null) {
            instance = new ProximityOutListener();
            //SerialSubsystem.getInstance().registerListener("PRXYO", instance); TEMPORARILY COMMENTED OUT - Needs to be migrated later
        }
        return instance;
    }
    
    @Override
    public void accept(String message) {
        if(message == "stop") {
            //tell the feeder motor to stop
        }
        else if(message == "decrement") {
            BallCount.getInstance().decrementBallCount();
        }
    }
}