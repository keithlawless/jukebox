package com.keithlawless.jukebox.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import com.keithlawless.jukebox.events.*;
import com.keithlawless.jukebox.services.MediaService;

import java.util.logging.Logger;

@Component
public class AppMediaEventListener {

    private static final Logger logger = Logger.getLogger(AppMediaEventListener.class.getName());

    @Autowired
    MediaService mediaService;

    @EventListener
    public void consumeMediaFinishedEvent(MediaFinishedEvent mediaFinishedEvent) {
        MusicResourceLocator mrl = mediaService.playNext();
    }

    @EventListener
    public void consumeMediaMetaEvent(MediaMetaEvent mediaMetaEvent) {
        mediaService.setMeta(mediaMetaEvent.getMediaMeta());
    }

    @EventListener
    public void consumeMediaPausedEvent(MediaPausedEvent mediaPausedEvent) {
        mediaService.setPlayState(mediaPausedEvent.getPlayState());
    }

    @EventListener
    public void consumeMediaPlayingEvent(MediaPlayingEvent mediaPlayingEvent) {
        mediaService.setPlayState(mediaPlayingEvent.getPlayState());
    }

    @EventListener
    public void consumeMediaStoppedEvent(MediaStoppedEvent mediaStoppedEvent) {
        mediaService.setPlayState(mediaStoppedEvent.getPlayState());
    }

    @EventListener
    public void consumerMediaTimingEvent(MediaTimingEvent mediaTimingEvent) {
        mediaService.setElapsedTime(mediaTimingEvent.getTime());
    }
}
