package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.services.TagService;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/read")
    public MediaMeta read(@RequestParam(required = true) String mrl) {
        return tagService.readTags(mrl);
    }
}
