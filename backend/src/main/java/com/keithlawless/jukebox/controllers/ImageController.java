package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.keithlawless.jukebox.entity.Artwork;
import com.keithlawless.jukebox.services.ImageService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/image")
public class ImageController {

    private static final Logger logger = Logger.getLogger(ImageController.class.getName());

    @Autowired
    private ImageService imageService;

    @GetMapping("/fetch")
    public ResponseEntity<byte[]> fetch(@RequestParam(required = true) String mrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        Artwork artwork = imageService.fetchArtwork(mrl);
        headers.set(HttpHeaders.CONTENT_TYPE, artwork.getMimeType());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(artwork.getImageBytes(), headers, HttpStatus.OK);
        return responseEntity;
    }
}
