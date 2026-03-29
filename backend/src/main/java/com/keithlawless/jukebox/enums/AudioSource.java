package com.keithlawless.jukebox.enums;

public enum AudioSource {
    LOCAL(1),
    RADIO(2),
    SPOTIFY(3);

    private int source;

    private AudioSource(int source) {
        this.source = source;
    }

    public int getSource() {
        return source;
    }
}
