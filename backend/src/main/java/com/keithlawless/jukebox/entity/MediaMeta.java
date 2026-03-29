package com.keithlawless.jukebox.entity;

import com.keithlawless.jukebox.enums.AudioSource;
import com.keithlawless.jukebox.enums.PlayState;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents metadata for a media file including title, artist, album, and other details
 */
@Schema(description = "Represents metadata for a media file including title, artist, album, and other details")
public class MediaMeta implements Serializable {
    @Schema(description = "Media Resource Locator (MRL) of the media file",
            example = "file:///music/queen/bohemian_rhapsody.mp3")
    private String mrl;

    @Schema(description = "Name of the artist or band",
            example = "Queen")
    private String artist;

    @Schema(description = "Name of the album containing the media",
            example = "A Night at the Opera")
    private String album;

    @Schema(description = "Title of the media track",
            example = "Bohemian Rhapsody")
    private String title;

    @Schema(description = "Duration of the media in milliseconds",
            example = "354000")
    private long duration;

    @Schema(description = "Elapsed playback time in milliseconds",
            example = "120000")
    private long elapsedTime;

    @Schema(description = "Current playback state of the media",
            implementation = PlayState.class)
    private PlayState playState;
    private AudioSource audioSource;

    @Schema(description = "Track number of the media",
            example = "1")
    private String trackNumber;

    public String getMrl() {
        return mrl;
    }

    public void setMrl(String mrl) {
        this.mrl = mrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public PlayState getPlayState() {
        return playState;
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public String getTrackNumber() { return trackNumber; }

    public void setTrackNumber(String trackNumber) { this.trackNumber = trackNumber; }


    public AudioSource getAudioSource() {
        return audioSource;
    }

    public void setAudioSource(AudioSource audioSource) {
        this.audioSource = audioSource;
    }
}
