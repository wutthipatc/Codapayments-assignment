package com.codapayments.roundrobin.controller.external;

import com.codapayments.roundrobin.dto.external.ApiExchange;
import com.codapayments.roundrobin.service.ExternalApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("application")
public class ExternalApiController {
    private final ExternalApiService externalApiService;
    // To be able to check the state and start service from docker-compose in order
    @GetMapping("health")
    public String getHealthCheck() {
        return "Healthy";
    }
    @PostMapping
    public ApiExchange executeExternalApi(@RequestBody ApiExchange request) {
        return externalApiService.callApplicationService(request);
    }
}
