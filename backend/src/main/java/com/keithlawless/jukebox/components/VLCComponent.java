package com.keithlawless.jukebox.components;

import com.google.common.net.UrlEscapers;
import com.keithlawless.jukebox.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.keithlawless.jukebox.entity.MediaMeta;
import org.yaml.snakeyaml.util.UriEncoder;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.*;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.base.State;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Component
public class VLCComponent implements MediaPlayerEventListener, MediaEventListener {

    private static final Logger logger = Logger.getLogger(VLCComponent.class.getName());

    @Autowired
    private AppMediaEventPublisher appMediaEventPublisher;

    @Autowired
    private TagService tagService;

    private MediaPlayerFactory mediaPlayerFactory;
    private MediaPlayer mediaPlayer;

    @PostConstruct
    public void init() {
        logger.info("Initializing VLCComponent...");

        mediaPlayerFactory = new MediaPlayerFactory("--aout", "alsa");
        logger.info("VLC found at path: " + mediaPlayerFactory.nativeLibraryPath());

        mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
        mediaPlayer.events().addMediaPlayerEventListener(this);
        mediaPlayer.events().addMediaEventListener(this);
        logger.info("MediaPlayer created...");

        logger.info("VLComponent initialization completed...");
    }

    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up VLCComponent...");
        if(mediaPlayerFactory != null) {
            mediaPlayerFactory.release();
        }
        logger.info("VLCComponent cleanup completed...");
    }

    public void play(String mediaResourceLocator) {
        if(mediaPlayer == null) {
            logger.info("No media player found when attempting to PLAY media?");
            return;
        }

        URI uri = URI.create(UrlEscapers.urlFragmentEscaper().escape(mediaResourceLocator));
        logger.info("In VLCComponent, playing: " + uri.toString());
        mediaPlayer.media().play(uri.toString());

    }

    public void pause() {
        if(mediaPlayer == null) {
            logger.info("No media player found when attempting to PAUSE media?");
            return;
        }

        mediaPlayer.controls().pause();

    }

    public void play() {
        if(mediaPlayer == null) {
            logger.info("No media player found when attempting to PLAY media?");
            return;
        }

        mediaPlayer.controls().play();

    }

    public void start() {
        if(mediaPlayer == null) {
            logger.info("No media player found when attempting to START media?");
            return;
        }

        mediaPlayer.controls().start();

    }

    public void stop() {
        if(mediaPlayer == null) {
            logger.info("No media player found when attempting to STOP media?");
            return;
        }

        mediaPlayer.controls().stop();

    }

    // MediaPlayerEventListener overrides

    @Override
    public void mediaChanged(MediaPlayer mediaPlayer, MediaRef media) {

    }

    @Override
    public void opening(MediaPlayer mediaPlayer) {

    }

    @Override
    public void buffering(MediaPlayer mediaPlayer, float newCache) {

    }

    @Override
    public void playing(MediaPlayer mediaPlayer) {
        appMediaEventPublisher.publishMediaPlayingEvent();
    }

    @Override
    public void paused(MediaPlayer mediaPlayer) {
        appMediaEventPublisher.publishMediaPausedEvent();
    }

    @Override
    public void stopped(MediaPlayer mediaPlayer) {
        appMediaEventPublisher.publishMediaStoppedEvent();
    }

    @Override
    public void forward(MediaPlayer mediaPlayer) {

    }

    @Override
    public void backward(MediaPlayer mediaPlayer) {

    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        appMediaEventPublisher.publishMediaFinishedEvent(mediaPlayer.media().info().mrl());
    }

    @Override
    public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
        appMediaEventPublisher.publishMediaTimingEvent(newTime);
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {

    }

    @Override
    public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable) {

    }

    @Override
    public void pausableChanged(MediaPlayer mediaPlayer, int newPausable) {

    }

    @Override
    public void titleChanged(MediaPlayer mediaPlayer, int newTitle) {

    }

    @Override
    public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {

    }

    @Override
    public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {

    }

    @Override
    public void videoOutput(MediaPlayer mediaPlayer, int newCount) {

    }

    @Override
    public void scrambledChanged(MediaPlayer mediaPlayer, int newScrambled) {

    }

    @Override
    public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType type, int id) {

    }

    @Override
    public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType type, int id) {

    }

    @Override
    public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType type, int id) {

    }

    @Override
    public void corked(MediaPlayer mediaPlayer, boolean corked) {

    }

    @Override
    public void muted(MediaPlayer mediaPlayer, boolean muted) {

    }

    @Override
    public void volumeChanged(MediaPlayer mediaPlayer, float volume) {

    }

    @Override
    public void audioDeviceChanged(MediaPlayer mediaPlayer, String audioDevice) {

    }

    @Override
    public void chapterChanged(MediaPlayer mediaPlayer, int newChapter) {

    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        logger.info("MediaPlayer error was reported...");
    }

    @Override
    public void mediaPlayerReady(MediaPlayer mediaPlayer) {

    }

    // MediaEventListener overrides

    @Override
    public void mediaMetaChanged(Media media, Meta metaType) {
    }

    @Override
    public void mediaSubItemAdded(Media media, MediaRef newChild) {

    }

    @Override
    public void mediaDurationChanged(Media media, long newDuration) {

    }

    @Override
    public void mediaParsedChanged(Media media, MediaParsedStatus newStatus) {
        if(newStatus == MediaParsedStatus.DONE) {
            try {
                String mrl = URLDecoder.decode(media.info().mrl(), StandardCharsets.UTF_8.name());
                MediaMeta mediaMeta = tagService.readTags(mrl);
                mediaMeta.setDuration(media.info().duration());
                appMediaEventPublisher.publishMediaMetaEvent(mediaMeta);
            }
            catch(UnsupportedEncodingException e) {
                logger.info("UnsupportedEncodingException caught: " + e.toString());
            }
        }
    }

    @Override
    public void mediaFreed(Media media, MediaRef mediaFreed) {

    }

    @Override
    public void mediaStateChanged(Media media, State newState) {

    }

    @Override
    public void mediaSubItemTreeAdded(Media media, MediaRef item) {

    }

    @Override
    public void mediaThumbnailGenerated(Media media, Picture picture) {
    }
}

