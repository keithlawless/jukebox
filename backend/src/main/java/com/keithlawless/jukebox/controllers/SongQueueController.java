package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.entity.MetaList;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import com.keithlawless.jukebox.services.MediaService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/queue")
public class SongQueueController  {
    private static final Logger logger = Logger.getLogger(SongQueueController.class.getName());

    @Autowired
    private MediaService mediaService;

    @PostMapping("/add")
    public MusicResourceLocator add(@RequestBody MusicResourceLocator musicResourceLocator) {
        return mediaService.addToPlayQueue(musicResourceLocator);
    }

    @PostMapping("/addmany")
    public Integer addmany(@RequestBody String[] songList) {
        for(String song: songList) {
            MusicResourceLocator mrl = new MusicResourceLocator();
            mrl.setMrl(song);
            mediaService.addToPlayQueue(mrl);
        }
        return songList.length;
    }

    @GetMapping("/playing")
    public MediaMeta playing() {
        MediaMeta mediaMeta = mediaService.getMeta();
        if(mediaMeta == null) {
            return null;
        }
        else {
            return mediaMeta;
        }
    }

    @GetMapping("/list")
    public MetaList list() {
        return mediaService.getMetaList();
    }

    @PostMapping("/empty")
    public void empty() { mediaService.drainQueue(); }
}
