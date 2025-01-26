package com.heony.coffee_order_collector._common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<String> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.httpStatusCode()).body(e.body());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(Exception e) {
        log.error("exception", e);

        int statusCode = 500;
        try{
            statusCode = HttpStatus.valueOf(e.getMessage()).value();
        }catch (Exception ignore){}

        return ResponseEntity.status(statusCode).body(e.getMessage());
    }
}
