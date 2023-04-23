package com.codapayments.application.model;

import com.codapayments.application.dto.register.request.ServiceRegisterRequest;
import lombok.*;

import java.time.Instant;
import java.util.Optional;

@Builder(access = AccessLevel.PRIVATE)
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceInfo {
    private final String name;
    private final String baseUrl;
    private final Instant lastUpdateTime;
    private final Boolean isSlow;
    public ServiceInfo copy(
            Optional<String> nameOption,
            Optional<String> baseUrlOption,
            Optional<Instant> updateTimeOption,
            Optional<Boolean> isSlowOption
    ) {
        String name = nameOption.orElse(this.name);
        String baseUrl = baseUrlOption.orElse(this.baseUrl);
        Instant updateTime = updateTimeOption.orElse(this.lastUpdateTime);
        Boolean isSlow = isSlowOption.orElse(this.isSlow);
        return ServiceInfo.builder()
                .name(name)
                .baseUrl(baseUrl)
                .lastUpdateTime(updateTime)
                .isSlow(isSlow)
                .build();
    }
    public static ServiceInfo initFromNameAndBaseUrl(String name, String baseUrl) {
        return ServiceInfo.builder()
                .name(name)
                .baseUrl(baseUrl)
                .lastUpdateTime(Instant.now())
                .isSlow(false)
                .build();
    }
    public static ServiceInfo fromServiceRegisterReq(ServiceRegisterRequest request) {
        return ServiceInfo.builder()
                .name(request.getName())
                .baseUrl(request.getBaseUrl())
                .lastUpdateTime(request.getLastUpdateTime())
                .isSlow(request.getIsSlow())
                .build();
    }
}
