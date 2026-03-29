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

import java.util.logging.Logger;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

@RestController
@RequestMapping("/radio")
public class InternetRadioController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private InternetRadioService internetRadioService;


    Logger logger = Logger.getLogger(InternetRadioController.class.getName());

    @PostMapping("/play")
    public MusicResourceLocator play(HttpServletRequest request) {
        String rawPayload = null;
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            rawPayload = sb.toString();
        } catch (Exception e) {
            logger.severe("Error reading request body: " + e.getMessage());
            throw new RuntimeException("Failed to read request body", e);
        }
        
        logger.info("Raw payload received: " + rawPayload);
        
        MusicResourceLocator musicResourceLocator;
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            musicResourceLocator = mapper.readValue(rawPayload, MusicResourceLocator.class);
        } catch (Exception e) {
            logger.severe("Error deserializing payload: " + e.getMessage());
            throw new RuntimeException("Failed to deserialize MusicResourceLocator", e);
        }
        
        mediaService.stop();
        logger.info("Playing internet radio station: " + musicResourceLocator.getMrl());
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
