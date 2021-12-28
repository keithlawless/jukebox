package com.keithlawless.jukebox.controllers;

import com.keithlawless.jukebox.entity.RadioStations;
import com.keithlawless.jukebox.services.InternetRadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import com.keithlawless.jukebox.services.MediaService;

@RestController
@RequestMapping("/radio")
public class InternetRadioController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private InternetRadioService internetRadioService;

    @PostMapping("/play")
    public MusicResourceLocator play(@RequestBody MusicResourceLocator musicResourceLocator) {
        mediaService.stop();
        return mediaService.play(musicResourceLocator);
    }

    @GetMapping("/stop")
    public void stop() {
        mediaService.stopInternetRadio();
    }

    @GetMapping("/list")
    public RadioStations get() {
        return internetRadioService.getStations();
    }
}
