package com.codapayments.application.controller;

import com.codapayments.application.dto.external.ApiExchange;
import com.codapayments.application.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("application")
public class ExternalApiController {
    private final ExternalApiService apiService;
    // To be able to check the state and start service from docker-compose in order
    @GetMapping("health")
    public String getHealthCheck() {
        return "Healthy";
    }
    @PostMapping
    public ApiExchange callApplication(@RequestBody ApiExchange request) {
        return apiService.callApplication(request);
    }
}
