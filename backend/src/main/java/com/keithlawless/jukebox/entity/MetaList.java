package com.keithlawless.jukebox.entity;

import java.util.List;

public class MetaList {
    private int size;
    private List<MediaMeta> queue;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<MediaMeta> getQueue() {
        return queue;
    }

    public void setQueue(List<MediaMeta> queue) {
        this.queue = queue;
    }
}
