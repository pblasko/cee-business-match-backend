package com.cee.business_match_backend.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health", description = "Health check endpoint")
public class HealthController {

    @Operation(summary = "Health check", description = "Simple endpoint to verify backend is running")
    @GetMapping("/api/health")
    public String health() {
        return "Backend is running";
    }

}
