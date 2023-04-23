package com.codapayments.roundrobin.service;

import com.codapayments.roundrobin.dto.external.ApiExchange;
import com.codapayments.roundrobin.model.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

@Service
public class ExternalApiService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);
    private Integer currentServiceIndex;
    private final ServiceRegisterService registerService;
    private final WebClient httpClient;
    public ExternalApiService(WebClient.Builder builder, ServiceRegisterService registerService) {
        // First call on first instance
        this.currentServiceIndex = 1;
        this.registerService = registerService;
        this.httpClient = builder.build();
    }
    private synchronized Optional<ServiceInfo> getAndUpdateInstantIndex() {
        final Instant start = Instant.now();
        return registerService.getAvailableServiceInfoWithNextIndexByIndexKey(currentServiceIndex)
                .map(nextIndexServiceInfoTuple -> {
                    logger.info("ExternalApiService::getAndUpdateInstantIndex consume time on getting service: {}", Instant.now().toEpochMilli() - start.toEpochMilli());
                    currentServiceIndex = nextIndexServiceInfoTuple.getT1();
                    return nextIndexServiceInfoTuple.getT2();
                });
    }
    public ApiExchange callApplicationService(ApiExchange request) {
        logger.info("ExternalApiService::callApplicationService with request: {}", request);
        final Optional<ServiceInfo> serviceInfoOption = getAndUpdateInstantIndex();
        logger.info("ExternalApiService::callApplicationService call with service info: {}", serviceInfoOption);
        return serviceInfoOption.map(serviceInfo -> httpClient.post()
                    .uri(URI.create(String.format("%s/%s", serviceInfo.getBaseUrl(), "application")))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(request), ApiExchange.class)
                    .retrieve()
                    .bodyToMono(ApiExchange.class)
                    .doOnError(Throwable::printStackTrace)
                    .onErrorReturn(ApiExchange.getErrorResponse(String.format("Something went wrong when call application service: %s", serviceInfo.getName())))
                    .block()
        ).orElseGet(() -> ApiExchange.getErrorResponse("There is no application instance available"));
    }
}
