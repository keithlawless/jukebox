package com.keithlawless.jukebox.events;

import org.springframework.context.ApplicationEvent;
import com.keithlawless.jukebox.entity.MediaMeta;

public class MediaMetaEvent extends ApplicationEvent {
    private MediaMeta mediaMeta;

    public MediaMetaEvent(Object source, MediaMeta mediaMeta) {
        super(source);
        this.mediaMeta = mediaMeta;
    }

    public MediaMeta getMediaMeta() {
        return mediaMeta;
    }
}
