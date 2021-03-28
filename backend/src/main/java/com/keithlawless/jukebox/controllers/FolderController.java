package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.keithlawless.jukebox.entity.Folder;
import com.keithlawless.jukebox.services.FileService;

@RestController
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public Folder list(@RequestParam(required = false) String entryPoint) {
        return fileService.getFolder(entryPoint);
    }
}
