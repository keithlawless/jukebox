package com.keithlawless.jukebox.entity;

import java.util.List;

public class QueueList {
    private int size;
    private List<MusicResourceLocator> queue;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<MusicResourceLocator> getQueue() {
        return queue;
    }

    public void setQueue(List<MusicResourceLocator> queue) {
        this.queue = queue;
    }
}
