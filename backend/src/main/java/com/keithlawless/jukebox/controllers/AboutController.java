package com.keithlawless.jukebox.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/about")
public class AboutController {
    private static final Logger logger = Logger.getLogger(AboutController.class.getName());

    @Value("${app.version}")
    private String version;

    @GetMapping("/version")
    @Operation(summary = "Get application version",
               description = "Returns the current version number of the application")
    @ApiResponse(responseCode = "200",
                description = "Version retrieved successfully")
    public Map<String, String> getVersion() {
        Map<String, String> response = new HashMap<>();
        response.put("version", version);
        return response;
    }
}
