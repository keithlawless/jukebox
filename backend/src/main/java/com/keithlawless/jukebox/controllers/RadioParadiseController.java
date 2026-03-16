package com.keithlawless.jukebox.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.logging.Logger;

@RestController
@RequestMapping("/radioparadise")
public class RadioParadiseController {

    private static final Logger logger = Logger.getLogger(RadioParadiseController.class.getName());
    private static final String RADIO_PARADISE_API_URL = "https://api.radioparadise.com/api/now_playing?chan=";

    private final RestTemplate restTemplate;

    public RadioParadiseController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/nowplaying")
    @Operation(summary = "Get Radio Paradise now playing information",
               description = "Proxies the Radio Paradise API to get currently playing track information for a specific channel")
    @ApiResponse(responseCode = "200", 
                description = "Successfully retrieved now playing information")
    @ApiResponse(responseCode = "400", 
                description = "Invalid channel parameter")
    public ResponseEntity<String> getNowPlaying(
            @Parameter(description = "Radio Paradise channel (0-7)", 
                      required = true,
                      example = "0")
            @RequestParam(required = true) int chan) {
        
        if (chan < 0 || chan > 7) {
            logger.warning("Invalid channel parameter: " + chan);
            return ResponseEntity.badRequest().body("{\"error\":\"Channel must be between 0 and 7\"}");
        }

        try {
            String url = RADIO_PARADISE_API_URL + chan;
            logger.info("Proxying request to Radio Paradise API: " + url);
            
            String response = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.severe("Error calling Radio Paradise API: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Failed to fetch data from Radio Paradise API\"}");
        }
    }
}
