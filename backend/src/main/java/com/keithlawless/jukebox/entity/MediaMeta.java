package com.keithlawless.jukebox.entity;

import com.keithlawless.jukebox.enums.PlayState;

import java.io.Serializable;

public class MediaMeta implements Serializable {
    private String mrl;
    private String artist;
    private String album;
    private String title;
    private PlayState playState;

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

    public PlayState getPlayState() {
        return playState;
    }

    public void setPlayState(PlayState playState) {
        this.playState = playState;
    }
}
