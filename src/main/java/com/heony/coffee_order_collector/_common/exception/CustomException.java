package com.heony.coffee_order_collector._common.exception;

public class CustomException extends RuntimeException {
    private final int httpStatusCode;
    private final String body;
    private final String message;

    public CustomException(ErrorCodes errorCode){
        super(errorCode.message());
        this.httpStatusCode = errorCode.httpStatusCode();
        this.body = errorCode.name();
        this.message = errorCode.message();
    }

    public CustomException(ErrorCodes errorCode, String message){
        super(errorCode.message());
        this.httpStatusCode = errorCode.httpStatusCode();
        this.body = errorCode.name();
        this.message = message;
    }

    public CustomException(ErrorCodes errorCode, boolean customWithParentheses, String message){
        super(customWithParentheses ? String.format("%s (%s)", errorCode.message(), message) : errorCode.message());
        this.httpStatusCode = errorCode.httpStatusCode();
        this.body = errorCode.name();
        this.message = customWithParentheses ? String.format("%s (%s)", errorCode.message(), message) : errorCode.message();

    }

    public CustomException(int httpStatusCode, String body, String message) {
        this.httpStatusCode = httpStatusCode;
        this.body = body;
        this.message = message;
    }

    public int httpStatusCode() {
        return httpStatusCode;
    }

    public String body() {
        return body;
    }
    public String message() {
        return message;
    }
}
