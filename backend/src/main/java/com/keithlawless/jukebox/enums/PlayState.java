package com.keithlawless.jukebox.enums;

public enum PlayState {
    PLAYING(1),
    PAUSED(2),
    STOPPED(3),
    QUEUED(4);

    private int state;

    private PlayState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
