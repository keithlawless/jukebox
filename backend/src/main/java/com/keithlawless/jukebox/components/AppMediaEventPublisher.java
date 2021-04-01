package com.keithlawless.jukebox.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.events.*;

@Component
public class AppMediaEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishMediaFinishedEvent(final String mrl) {
        MediaFinishedEvent mediaFinishedEvent = new MediaFinishedEvent(this, mrl);
        applicationEventPublisher.publishEvent(mediaFinishedEvent);
    }

    public void publishMediaMetaEvent(final MediaMeta mediaMeta) {
        MediaMetaEvent mediaMetaEvent = new MediaMetaEvent(this, mediaMeta);
        applicationEventPublisher.publishEvent(mediaMetaEvent);
    }

    public void publishMediaPlayingEvent() {
        MediaPlayingEvent mediaPlayingEvent = new MediaPlayingEvent(this);
        applicationEventPublisher.publishEvent(mediaPlayingEvent);
    }

    public void publishMediaPausedEvent() {
        MediaPausedEvent mediaPausedEvent = new MediaPausedEvent(this);
        applicationEventPublisher.publishEvent(mediaPausedEvent);
    }

    public void publishMediaStoppedEvent() {
        MediaStoppedEvent mediaStoppedEvent = new MediaStoppedEvent(this);
        applicationEventPublisher.publishEvent(mediaStoppedEvent);
    }

    public void publishMediaTimingEvent(long time) {
        MediaTimingEvent mediaTimingEvent = new MediaTimingEvent(this, time);
        applicationEventPublisher.publishEvent(mediaTimingEvent);
    }
}
