package com.codapayments.application.service;

import com.codapayments.application.dto.register.request.ServiceRegisterRequest;
import com.codapayments.application.dto.register.response.RegisterAckResponse;
import com.codapayments.application.model.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Executors;

@Service
public class SimpleHeartBeatService {
    private static final Logger logger = LoggerFactory.getLogger(SimpleHeartBeatService.class);
    private static final Integer HEART_BEAT_INTERVAL_MILLI = 2000;
    // State of this service
    private ServiceInfo serviceInfo;
    @Value("${register-server.url:http://localhost:8080/register}")
    private String registerServerUrl;
    @Value("${service.name:application-api}")
    private String serviceName;
    @Value("${service.base-url:http://localhost:8081}")
    private String serviceBaseUrl;
    @Value("${application.reset-slow-duration.milli:200}")
    private Integer resetIsSlowDurationMilli;
    private final WebClient webClient;
    public SimpleHeartBeatService(WebClient.Builder builder) {
        webClient = builder.build();
    }
    @PostConstruct
    private void init() {
        serviceInfo = ServiceInfo.initFromNameAndBaseUrl(serviceName, serviceBaseUrl);
        Executors.newSingleThreadExecutor().submit(callRegisterEndpointInterval());
    }
    public synchronized void updateIsSlowServiceInfo(Boolean isSlow) {
        serviceInfo = serviceInfo.copy(Optional.empty(), Optional.empty(), Optional.of(Instant.now()), Optional.of(isSlow));
    }
    public void updateResetIsSlowDuration(Integer milli) {
        Optional.ofNullable(milli).ifPresent(value -> resetIsSlowDurationMilli = value);
    }
    private void resetIsSlowFalse() {
        if (serviceInfo.getIsSlow() && serviceInfo.getLastUpdateTime().plusMillis(resetIsSlowDurationMilli).isBefore(Instant.now()))
            updateIsSlowServiceInfo(false);
    }
    private Runnable callRegisterEndpointInterval() {
        return () -> {
            while(true) {
                try {
                    resetIsSlowFalse();
                    final ServiceInfo serviceInfoReq = serviceInfo.copy(
                            Optional.empty(),
                            Optional.empty(),
                            Optional.of(Instant.now()),
                            Optional.empty()
                    );
//                    logger.info("SimpleHeartBeatService::callRegisterEndpointInterval with service info: {}", serviceInfoReq);
                    webClient.post()
                            .uri(URI.create(registerServerUrl))
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .body(Mono.just(ServiceRegisterRequest.fromServiceInfo(serviceInfoReq)), ServiceRegisterRequest.class)
                            .retrieve()
                            .bodyToMono(RegisterAckResponse.class)
                            .block();
                    Thread.sleep(HEART_BEAT_INTERVAL_MILLI);
                } catch (Throwable e) {
                    logger.error("SimpleHeartBeatService::callRegisterEndpointInterval error occurred with message: {}", e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
