package com.keithlawless.jukebox.events;

import org.springframework.context.ApplicationEvent;
import com.keithlawless.jukebox.enums.PlayState;

public class MediaPlayingEvent extends ApplicationEvent {

    private PlayState playState;

    public MediaPlayingEvent(Object source) {
        super(source);
        this.playState = PlayState.PLAYING;
    }

    public PlayState getPlayState() {
        return this.playState;
    }
}
