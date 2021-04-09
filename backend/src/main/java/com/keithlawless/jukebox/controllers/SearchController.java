package com.keithlawless.jukebox.controllers;

import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping("/all")
    public List<MediaMeta> searchAll(@RequestParam(required = true) String searchTerm) {
        return searchService.query(searchTerm);
    }
}
