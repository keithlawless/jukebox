package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.keithlawless.jukebox.entity.Artwork;
import com.keithlawless.jukebox.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.logging.Logger;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static final Logger logger = Logger.getLogger(ImageController.class.getName());

    @Autowired
    private ImageService imageService;

    @GetMapping("/fetch")
    @Operation(summary = "Fetch artwork image",
               description = "Retrieves an artwork image for the specified media resource location (MRL)")
    @ApiResponse(responseCode = "200", 
                description = "Successfully retrieved artwork image",
                content = @Content(mediaType = "image/*"))
    @ApiResponse(responseCode = "404", 
                description = "Artwork not found for the specified MRL")
    public ResponseEntity<byte[]> fetch(
            @Parameter(description = "Media Resource Location (MRL) of the media file", 
                      required = true,
                      example = "file:///music/album/track.mp3")
            @RequestParam(required = true) String mrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        Artwork artwork = imageService.fetchArtwork(mrl);
        headers.set(HttpHeaders.CONTENT_TYPE, artwork.getMimeType());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(artwork.getImageBytes(), headers, HttpStatus.OK);
        return responseEntity;
    }
}
