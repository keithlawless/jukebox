package com.keithlawless.jukebox.controllers;

import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.services.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/status")
    @Operation(summary = "Get search index status",
               description = "Returns the current status of the search index (ready, indexing, or not started)")
    @ApiResponse(responseCode = "200", 
                description = "Status retrieved successfully")
    public Map<String, Object> getIndexStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("indexReady", searchService.isIndexReady());
        status.put("indexing", searchService.isIndexing());
        return status;
    }

    @GetMapping("/all")
    @Operation(summary = "Search for media",
               description = "Searches across all media metadata for the given search term")
    @ApiResponse(responseCode = "200", 
                description = "Search completed successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = MediaMeta.class))))
    @ApiResponse(responseCode = "400", 
                description = "Invalid search term or search failed")
    public List<MediaMeta> searchAll(
            @Parameter(description = "Search term to look for in media metadata", 
                      required = true,
                      example = "jazz")
            @RequestParam() String searchTerm) {
        return searchService.query(searchTerm);
    }
}
