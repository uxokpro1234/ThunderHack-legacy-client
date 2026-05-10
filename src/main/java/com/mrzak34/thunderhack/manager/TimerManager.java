package com.mrzak34.thunderhack.manager;


public class TimerManager {
    private float timer;

    public TimerManager() {
        this.timer = 1.0f;
    }

    public void setTimer(final float timer) {
        if (timer > 0.0f) {
            this.timer = timer;
        }
    }
}

