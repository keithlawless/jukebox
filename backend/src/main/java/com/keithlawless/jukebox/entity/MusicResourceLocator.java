package com.keithlawless.jukebox.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Represents a media resource location with its MRL (Media Resource Locator)
 */
@Schema(description = "Represents a media resource location with its MRL (Media Resource Locator)")
public class MusicResourceLocator implements Serializable {

    @Schema(description = "Media Resource Locator (MRL) string that identifies the media resource",
            example = "file:///music/queen/bohemian_rhapsody.mp3")
    private String mrl;

    public MusicResourceLocator() {}
    public MusicResourceLocator(String mrl) {
        this.mrl = mrl;
    }

    public String getMrl() {
        return mrl;
    }

    public void setMrl(String mrl) {
        this.mrl = mrl;
    }

}
