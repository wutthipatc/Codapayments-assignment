package com.codapayments.application.service;

import com.codapayments.application.dto.external.ApiExchange;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExternalApiService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);
    @Value("${service.name:application-api}")
    private String serviceName;
    @Value("${application.slow-duration-milli:1000}")
    private Integer slowDurationQualificationMilli;
    @Value("${application.processing-duration-milli:200}")
    private Integer mockProcessingDurationMilli;
    private final SimpleHeartBeatService heartBeatService;
    public ApiExchange callApplication(ApiExchange request) {
        logger.info("ExternalApiService::callApplication with request: {}", request);
        final Instant start = Instant.now();
        try {
            Thread.sleep(mockProcessingDurationMilli);
            heartBeatService.updateIsSlowServiceInfo(isApiSlow(Instant.now().toEpochMilli() - start.toEpochMilli()));
            return ApiExchange.updateInstanceName(request, serviceName);
        } catch (Throwable e) {
            logger.error("ExternalApiService::callApplication error occurred with message: {}", e.getMessage(), e);
            return ApiExchange.getErrorResponse(String.format("Something went wrong when call application service: %s, error message: %s", serviceName, e.getMessage()));
        }
    }
    public synchronized void setMockApplicationConfig(Integer processingDuration, Integer slowDuration) {
        logger.info("ExternalApiService::setMockApplicationConfig with processingDuration: {}, slowDuration: {}", processingDuration, slowDuration);
        Optional.ofNullable(processingDuration).ifPresent(value -> mockProcessingDurationMilli = value);
        Optional.ofNullable(slowDuration).ifPresent(value -> slowDurationQualificationMilli = value);
    }
    private Boolean isApiSlow(Long processDurationMilli) {
        logger.info("ExternalApiService::isApiSlow with process duration: {}", processDurationMilli);
        return processDurationMilli > slowDurationQualificationMilli;
    }
}
