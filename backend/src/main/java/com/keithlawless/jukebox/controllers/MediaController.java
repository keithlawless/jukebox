package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import com.keithlawless.jukebox.services.MediaService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/media")
public class MediaController {

    private static final Logger logger = Logger.getLogger(MediaController.class.getName());

    @Autowired
    MediaService mediaService;

    @GetMapping("/")
    public String index() {
        return "Hello, there!";
    }

    @PostMapping("/play")
    public MusicResourceLocator play(@RequestBody MusicResourceLocator musicResourceLocator) {
        return mediaService.play(musicResourceLocator);
    }

    @GetMapping("/pause")
    public void pause() {
        mediaService.pause();
    }

    /*
     * Begin playing from the current location.
     */
    @GetMapping("/resume")
    public void resume() {
        mediaService.resume();
    }

    /*
     * Begin playing from the beginning of the media.
     */
    @GetMapping("/start")
    public void start() {
        mediaService.start();
    }

    @GetMapping("/stop")
    public void stop() {
        mediaService.stop();
    }

    @GetMapping("/next")
    public MusicResourceLocator next() { return mediaService.playNext(); }

}

