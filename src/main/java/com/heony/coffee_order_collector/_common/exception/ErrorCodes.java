package com.heony.coffee_order_collector._common.exception;

public enum ErrorCodes {

    NAME_USED_ALREADY(400, "이미 사용중인 이름 입니다."),
    ;

    private final int httpStatusCode;
    private final String message;

    ErrorCodes(int httpStatusCode, String message) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public int httpStatusCode() {
        return httpStatusCode;
    }

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
