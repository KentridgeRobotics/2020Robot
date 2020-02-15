package com.chargerrobotics.utils;

import java.util.function.Consumer;

import com.chargerrobotics.subsystems.SerialSubsystem;

public class ColorSpinnerSerialListener implements Consumer<String> {
    private static ColorSpinnerSerialListener instance = null;
    private String latestColor = null;

    public static ColorSpinnerSerialListener getInstance() {
        if (instance == null) {
            instance = new ColorSpinnerSerialListener();
            SerialSubsystem.getInstance().registerListener("COL", instance);
        }
        return instance;
    }

    @Override
    public void accept(String message) {
        try {
            latestColor = message;
        } catch (NumberFormatException e) {
            System.err.println("Bad message in SampleSerialListener: "+message);
        }
    }

    public String getColor() {
        return latestColor;
    }


}