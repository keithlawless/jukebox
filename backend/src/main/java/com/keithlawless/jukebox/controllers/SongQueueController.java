package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.entity.MetaList;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import com.keithlawless.jukebox.services.MediaService;

import java.util.List;
import java.util.logging.Logger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public Integer addmany(@RequestBody List<String> songList) {
        if (songList == null || songList.isEmpty()) {
            return 0;
        }
        for(String song: songList) {
            logger.info("Adding song " + song);
            MusicResourceLocator mrl = new MusicResourceLocator();
            mrl.setMrl(song);
            mediaService.addToPlayQueue(mrl);
        }
        return songList.size();
    }

    @GetMapping("/playing")
    public MediaMeta playing() {
        return mediaService.getMeta();
    }

    @GetMapping("/list")
    public MetaList list() {
        return mediaService.getMetaList();
    }

    @PostMapping("/empty")
    @Operation(summary = "Empty the queue",
               description = "Removes all songs from the playback queue")
    @ApiResponse(responseCode = "200", 
                description = "Queue emptied successfully")
    public void empty() { mediaService.drainQueue(); }
}
