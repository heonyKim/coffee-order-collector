package com.heony.coffee_order_collector._common.exception.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.heony.coffee_order_collector._common.exception.ErrorCodes;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetErrorResponse(
        int httpStatusCode,
        ErrorCodes code,
        String message
) {
    public static GetErrorResponse from(ErrorCodes errorCode) {
        return new GetErrorResponse(errorCode.httpStatusCode(), errorCode, errorCode.message());
    }
}
