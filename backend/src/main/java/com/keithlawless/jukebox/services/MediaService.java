package com.keithlawless.jukebox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.keithlawless.jukebox.components.SongQueue;
import com.keithlawless.jukebox.components.VLCComponent;
import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.entity.MetaList;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import com.keithlawless.jukebox.enums.PlayState;

import java.util.ArrayList;
import java.util.logging.Logger;

@Service
public class MediaService {
    private static final Logger logger = Logger.getLogger(MediaService.class.getName());

    @Autowired
    private TagService tagService;

    @Autowired
    VLCComponent vlcComponent;

    @Autowired
    SongQueue songQueue;

    @Value("${com.keithlawless.jukebox.controllers.autoplay}")
    private Boolean autoPlay;

    private MusicResourceLocator nowPlaying = null;
    private MediaMeta mediaMeta = null;
    private PlayState playState = null;

    public synchronized MusicResourceLocator addToPlayQueue(MusicResourceLocator musicResourceLocator) {
        int size = songQueue.add(musicResourceLocator);
        if(nowPlaying == null) {
            if(autoPlay) {
                playNext();
            }
        }
        return musicResourceLocator;
    }

    public synchronized MusicResourceLocator playNext() {
        MusicResourceLocator mrl = songQueue.poll();
        if(mrl != null) {
            play(mrl);
        }
        else {
            // If queue is empty, null out mediaMeta.
            mediaMeta = null;
        }
        nowPlaying = mrl;
        return mrl;
    }

    public synchronized MusicResourceLocator play(MusicResourceLocator musicResourceLocator) {
        if (musicResourceLocator != null) {
            String mrl = musicResourceLocator.getMrl();
            if (mrl != null) {
                vlcComponent.play(mrl);
            } else {
                logger.info("MusicResourceLocator object did not contain an mrl.");
            }
        } else {
            logger.info("No MusicResourceLocator object was passed to /play.");
        }

        return musicResourceLocator;
    }

    public void setMeta(MediaMeta mediaMeta) {
        this.mediaMeta = mediaMeta;
    }

    public MediaMeta getMeta() {
        if(this.mediaMeta != null) {
            this.mediaMeta.setPlayState(this.playState);
        }
        return this.mediaMeta;
    }

    public void pause() {
        vlcComponent.pause();
    }

    /*
     * Begin playing from the current location.
     */
    public void resume() {
        vlcComponent.start();
    }

    /*
     * Begin playing from the beginning of the media.
     */
    public void start() {
        vlcComponent.stop();
        vlcComponent.start();
    }

    public void stop() {
        vlcComponent.stop();
    }

    public MetaList getMetaList() {
        MetaList metaList = new MetaList();
        metaList.setSize(songQueue.size());

        ArrayList<MediaMeta> newList = new ArrayList<>();
        for( MusicResourceLocator mrl : songQueue.list()) {
            MediaMeta mediaMeta = tagService.readTags(mrl.getMrl());
            mediaMeta.setPlayState(PlayState.QUEUED);
            newList.add(mediaMeta);
        }

        metaList.setQueue(newList);
        return metaList;
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public void setElapsedTime(long time) { this.mediaMeta.setElapsedTime(time); }
}
