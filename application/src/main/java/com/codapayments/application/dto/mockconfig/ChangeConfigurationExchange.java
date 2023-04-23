package com.codapayments.application.dto.mockconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ChangeConfigurationExchange {
    private Integer processingDuration;
    private Integer slowDuration;
    private Integer resetIsSlowDuration;
}
