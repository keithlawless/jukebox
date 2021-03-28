package com.keithlawless.jukebox.events;

import org.springframework.context.ApplicationEvent;
import com.keithlawless.jukebox.enums.PlayState;

public class MediaPausedEvent extends ApplicationEvent {

    private PlayState playState;

    public MediaPausedEvent(Object source) {
        super(source);
        this.playState = PlayState.PAUSED;
    }

    public PlayState getPlayState() {
        return this.playState;
    }
}
