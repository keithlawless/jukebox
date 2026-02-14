package com.keithlawless.jukebox.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents an internet radio station with its stream URL and description
 */
@Schema(description = "Represents an internet radio station with its stream URL and description")
public class RadioStation {
    @Schema(description = "URL of the radio stream", 
            example = "http://stream.example.com:8000/stream")
    private String url;
    
    @Schema(description = "Human-readable description of the radio station", 
            example = "Best Classic Rock Radio")
    private String description;

    public RadioStation() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
