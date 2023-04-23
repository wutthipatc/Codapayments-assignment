package com.codapayments.roundrobin.dto.register.request;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ServiceRegisterRequest {
    private String name;
    private String baseUrl;
    private Instant lastUpdateTime;
    private Boolean isSlow;
}
