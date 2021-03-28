package com.keithlawless.jukebox.components;

import org.springframework.stereotype.Component;
import com.keithlawless.jukebox.entity.MusicResourceLocator;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class SongQueue {
    private static final Logger logger = Logger.getLogger(SongQueue.class.getName());

    private LinkedList<MusicResourceLocator> queue = (new LinkedList<MusicResourceLocator>());

    // Add a MusicResourceLocator to the queue.
    public synchronized int add(MusicResourceLocator musicResourceLocator) {
        queue.add(musicResourceLocator);
        return queue.size();
    }

    // Pop the queue. Immediately returns null if queue is empty.
    public synchronized MusicResourceLocator poll() {
        if(queue.peek() == null) {
            return null;
        }

        return queue.poll();
    }

    public int size() {
        return queue.size();
    }

    public List<MusicResourceLocator> list() {
        return queue;
    }
}

