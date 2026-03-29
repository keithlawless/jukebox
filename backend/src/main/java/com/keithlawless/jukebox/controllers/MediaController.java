package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import com.keithlawless.jukebox.enums.AudioSource;
import com.keithlawless.jukebox.services.MediaService;

import java.util.logging.Logger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    @Operation(summary = "Play next media",
               description = "Stops the current media and starts playing the next item in the playlist")
    @ApiResponse(responseCode = "200", 
                description = "Started playing next media",
                content = @Content(schema = @Schema(implementation = MusicResourceLocator.class)))
    @ApiResponse(responseCode = "404", 
                description = "No next media available")
    public MusicResourceLocator next() { return mediaService.playNext(); }

    @PostMapping("/spotify/activate")
    public void activateSpotify() {
        mediaService.switchToSpotifyMode();
    }

    @PostMapping("/spotify/deactivate")
    public void deactivateSpotify() {
        mediaService.switchToLocalMode();
    }

    @GetMapping("/source")
    public AudioSource getAudioSource() {
        return mediaService.getCurrentAudioSource();
    }

}

