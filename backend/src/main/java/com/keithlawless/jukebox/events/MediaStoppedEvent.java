package com.keithlawless.jukebox.events;

import org.springframework.context.ApplicationEvent;
import com.keithlawless.jukebox.enums.PlayState;

public class MediaStoppedEvent extends ApplicationEvent {

    private PlayState playState;

    public MediaStoppedEvent(Object source) {
        super(source);
        this.playState = PlayState.STOPPED;
    }

    public PlayState getPlayState() {
        return this.playState;
    }
}
