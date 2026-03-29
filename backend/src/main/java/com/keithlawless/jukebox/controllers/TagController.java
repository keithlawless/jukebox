package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.services.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/read")
    @Operation(summary = "Read media tags",
               description = "Reads and returns metadata tags from the specified media file")
    @ApiResponse(responseCode = "200", 
                description = "Successfully read media tags",
                content = @Content(schema = @Schema(implementation = MediaMeta.class)))
    @ApiResponse(responseCode = "400", 
                description = "Invalid MRL or unsupported media format")
    @ApiResponse(responseCode = "404", 
                description = "Media file not found")
    public MediaMeta read(
            @Parameter(description = "Media Resource Locator (MRL) of the media file", 
                      required = true,
                      example = "file:///path/to/media/file.mp3")
            @RequestParam(required = true) String mrl) {
        return tagService.readTags(mrl);
    }
}
