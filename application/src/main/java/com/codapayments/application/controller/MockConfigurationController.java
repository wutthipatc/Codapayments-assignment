package com.codapayments.application.controller;

import com.codapayments.application.dto.mockconfig.ChangeConfigurationExchange;
import com.codapayments.application.service.MockConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("configuration")
public class MockConfigurationController {
    private final MockConfigurationService mockConfigurationService;
    @PostMapping
    public ChangeConfigurationExchange changeConfig(@RequestBody ChangeConfigurationExchange request) {
        return mockConfigurationService.changeConfig(request);
    }
}
