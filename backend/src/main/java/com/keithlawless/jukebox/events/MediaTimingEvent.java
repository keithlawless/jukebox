package com.keithlawless.jukebox.events;

import org.springframework.context.ApplicationEvent;

public class MediaTimingEvent extends ApplicationEvent {
    private long time;

    public MediaTimingEvent(Object source, long time) {
        super(source);
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
