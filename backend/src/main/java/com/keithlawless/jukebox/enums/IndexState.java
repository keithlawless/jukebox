package com.keithlawless.jukebox.enums;

public enum IndexState {
    NONE(1),
    INDEXING(2),
    READY(3),
    ERROR(4);

    private int state;

    private IndexState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
