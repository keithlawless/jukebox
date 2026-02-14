package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import com.keithlawless.jukebox.entity.Folder;
import com.keithlawless.jukebox.services.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/folder")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET})
@Tag(name = "Folder Controller", description = "APIs for browsing and managing folder structures")
public class FolderController {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    @Operation(summary = "List folder contents",
               description = "Retrieves the contents of a specified folder or the root folder if no path is provided")
    @ApiResponse(responseCode = "200", 
                description = "Successfully retrieved folder contents",
                content = @Content(schema = @Schema(implementation = Folder.class)))
    @ApiResponse(responseCode = "404", description = "Folder not found")
    public Folder list(
            @Parameter(description = "Path to the folder to list (leave empty for root)", example = "/music/artists")
            @RequestParam(required = false) String entryPoint) {
        System.out.println("Requested entry point is: " + entryPoint);
        return fileService.getFolder(entryPoint);
    }
}
