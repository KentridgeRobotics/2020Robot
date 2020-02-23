package com.chargerrobotics.utils;

import java.nio.ByteBuffer;

import com.chargerrobotics.utils.ArduinoSerialReceiver.ArduinoListener;

public class GyroHeading extends ArduinoListener {

    private static double heading = 0.0;
    private static GyroHeading instance;
    private static final Object lock = new Object();
    public static GyroHeading getInstance() {
        if (instance == null) {
            instance = new GyroHeading();
            ArduinoSerialReceiver.registerListener(instance, (short)0x1337);
        }
        return instance;
    }
   
    private GyroHeading() {
        // private for singleton
    }
    public static double getHeading() {
        synchronized (lock) {
            return heading;
        }
    }

    @Override
    public void receiveData(ArduinoSerial serial, ByteBuffer data) {
        double newHeading = (double)data.getFloat();
        synchronized (lock) {
            heading = newHeading;
        }
    }
}
