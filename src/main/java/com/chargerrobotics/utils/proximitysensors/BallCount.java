package com.chargerrobotics.utils.proximitysensors;

public class BallCount {
    private static BallCount instance;
    private int ballCount = 0;

    public static BallCount getInstance() {
        if(instance == null) {
            instance = new BallCount();
        }
        return instance;
    }

    public void incrementBallCount() {
        ballCount++;
    }

    public void decrementBallCount() {
        ballCount--;
    }

    public int getBallCount() {
        return ballCount;
    }
}