package com.keithlawless.jukebox.events;

import org.springframework.context.ApplicationEvent;

public class MediaFinishedEvent extends ApplicationEvent {
    private String mrl;

    public MediaFinishedEvent(Object source, String mrl) {
        super(source);
        this.mrl = mrl;
    }

    public String getMrl() {
        return mrl;
    }
}
