package com.codapayments.application.service;

import com.codapayments.application.dto.mockconfig.ChangeConfigurationExchange;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MockConfigurationService {
    private static final Logger logger = LoggerFactory.getLogger(MockConfigurationService.class);
    private final ExternalApiService externalApiService;
    private final SimpleHeartBeatService heartBeatService;
    public ChangeConfigurationExchange changeConfig(ChangeConfigurationExchange request) {
        logger.info("MockConfigurationService::ChangeConfigurationExchange with request: {}", request);
        heartBeatService.updateResetIsSlowDuration(request.getResetIsSlowDuration());
        externalApiService.setMockApplicationConfig(request.getProcessingDuration(), request.getSlowDuration());
        return request;
    }
}
