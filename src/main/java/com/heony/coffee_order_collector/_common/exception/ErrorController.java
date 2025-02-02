package com.heony.coffee_order_collector._common.exception;

import com.heony.coffee_order_collector._common.exception.dto.GetErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/errors")
@RequiredArgsConstructor
public class ErrorController {

    @GetMapping("/{errorCode}")
    public ResponseEntity<GetErrorResponse> getError(@PathVariable String errorCode){
        ErrorCodes errorCodes = ErrorCodes.getByName(errorCode);
        if(errorCodes == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(GetErrorResponse.from(errorCodes));
    }
}
