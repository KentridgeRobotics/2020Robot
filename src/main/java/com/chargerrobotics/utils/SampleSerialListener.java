package com.chargerrobotics.utils;

import java.util.function.Consumer;

import com.chargerrobotics.subsystems.SerialSubsystem;

public class SampleSerialListener implements Consumer<String> {
    private static SampleSerialListener instance = null;
    private Integer latestNumber = null;

    public static SampleSerialListener getInstance() {
        if (instance == null) {
            instance = new SampleSerialListener();
            SerialSubsystem.getInstance().registerListener("SAMPLENUMBER", instance);
        }
        return instance;
    }

    @Override
    public void accept(String message) {
        try {
            latestNumber = Integer.parseInt(message);
        } catch (NumberFormatException e) {
            System.err.println("Bad message in SampleSerialListener: "+message);
        }
    }

    public Integer getLatestNumber() {
        return latestNumber;
    }


}