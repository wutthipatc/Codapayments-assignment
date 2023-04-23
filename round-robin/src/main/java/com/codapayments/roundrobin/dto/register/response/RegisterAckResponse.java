package com.codapayments.roundrobin.dto.register.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisterAckResponse {
    private String name;
    private Instant updateTime;
    public static RegisterAckResponse getSuccessResponse(String name, Instant updateTime) {
        return RegisterAckResponse.builder()
                .name(name)
                .updateTime(updateTime)
                .build();
    }
}
