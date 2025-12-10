package com.keithlawless.jukebox.controllers;

import com.keithlawless.jukebox.entity.RadioStations;
import com.keithlawless.jukebox.services.InternetRadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import com.keithlawless.jukebox.services.MediaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    @Operation(summary = "List available radio stations",
               description = "Retrieves the list of available internet radio stations")
    @ApiResponse(responseCode = "200", 
                description = "Successfully retrieved the list of radio stations",
                content = @Content(schema = @Schema(implementation = RadioStations.class)))
    public RadioStations get() {
        return internetRadioService.getStations();
    }
}
