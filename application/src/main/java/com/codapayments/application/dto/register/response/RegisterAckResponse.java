package com.codapayments.application.dto.register.response;

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
}
