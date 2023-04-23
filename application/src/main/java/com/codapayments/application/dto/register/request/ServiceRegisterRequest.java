package com.codapayments.application.dto.register.request;

import com.codapayments.application.model.ServiceInfo;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceRegisterRequest {
    private String name;
    private String baseUrl;
    private Instant lastUpdateTime;
    private Boolean isSlow;
    public static ServiceRegisterRequest fromServiceInfo(ServiceInfo serviceInfo) {
        return ServiceRegisterRequest.builder()
                .name(serviceInfo.getName())
                .baseUrl(serviceInfo.getBaseUrl())
                .lastUpdateTime(serviceInfo.getLastUpdateTime())
                .isSlow(serviceInfo.getIsSlow())
                .build();
    }
}
