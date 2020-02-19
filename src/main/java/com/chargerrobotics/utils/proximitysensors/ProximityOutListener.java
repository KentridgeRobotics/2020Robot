package com.chargerrobotics.utils.proximitysensors;

import java.util.function.Consumer;

import com.chargerrobotics.subsystems.SerialSubsystem;

public class ProximityOutListener implements Consumer<String> {
    private static ProximityOutListener instance = null;

    public static ProximityOutListener getInstance() {
        if(instance == null) {
            instance = new ProximityOutListener();
            SerialSubsystem.getInstance().registerListener("PRXYO", instance);
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