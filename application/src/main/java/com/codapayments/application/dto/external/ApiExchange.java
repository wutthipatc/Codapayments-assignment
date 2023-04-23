package com.codapayments.application.dto.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiExchange {
    private String game;
    private String gamerID;
    private Double points;
    private String instanceName;
    private String errorMsg;
    public static ApiExchange updateInstanceName(ApiExchange exchange, String instanceName) {
        return ApiExchange.builder()
                .game(exchange.getGame())
                .gamerID(exchange.getGamerID())
                .points(exchange.getPoints())
                .instanceName(instanceName)
                .build();
    }
    public static ApiExchange getErrorResponse(String errorMsg) {
        return ApiExchange.builder()
                .errorMsg(errorMsg)
                .build();
    }
}
