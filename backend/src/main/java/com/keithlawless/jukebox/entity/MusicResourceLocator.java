package com.keithlawless.jukebox.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Represents a media resource location with its MRL (Media Resource Locator)
 */
@Schema(description = "Represents a media resource location with its MRL (Media Resource Locator)")
public class MusicResourceLocator implements Serializable {

    @Schema(description = "Media Resource Locator (MRL) string that identifies the media resource",
            example = "file:///music/queen/bohemian_rhapsody.mp3")
    @JsonDeserialize(using = UrlDecodedStringDeserializer.class)
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

class UrlDecodedStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws java.io.IOException {
        String value = p.getValueAsString();
        if (value == null) {
            return null;
        }
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return value;
        }
    }
}
