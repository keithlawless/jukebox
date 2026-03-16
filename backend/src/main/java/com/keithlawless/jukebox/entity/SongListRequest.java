package com.keithlawless.jukebox.entity;

import java.util.List;

public class SongListRequest {
    private List<String> songs;

    public SongListRequest() {
    }

    public SongListRequest(List<String> songs) {
        this.songs = songs;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }
}
