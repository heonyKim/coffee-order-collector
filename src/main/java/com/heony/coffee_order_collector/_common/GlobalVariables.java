package com.heony.coffee_order_collector._common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Order(Integer.MIN_VALUE)
@Slf4j
public class GlobalVariables {

    public static long APP_UPDATE_AVAILABLE_TIME_MILLIES;

    @Autowired
    public void setAppUpdateAvailableTimeMillis(
            @Value("${app.update-available.timeout}") long timeout,
            @Value("${app.update-available.timeunit}") TimeUnit timeUnit
    ) {
        log.debug("update avaliable timeout : " + timeout + timeUnit);
        APP_UPDATE_AVAILABLE_TIME_MILLIES = convertMillis(timeout, timeUnit);

        if(APP_UPDATE_AVAILABLE_TIME_MILLIES == 0) {
            APP_UPDATE_AVAILABLE_TIME_MILLIES = convertMillis(2, TimeUnit.HOURS);
        }
    }

    private static long convertMillis(long timeout, TimeUnit timeUnit) {
        long millis = 0;
        try{
            if(timeUnit ==TimeUnit.DAYS){
                millis = 1000L * 60 * 60 * 24 * timeout;
                return millis;
            }
            if(timeUnit ==TimeUnit.HOURS){
                millis = 1000L * 60 * 60 * timeout;
                return millis;
            }
            if(timeUnit ==TimeUnit.MINUTES){
                millis = 1000L * 60 * timeout;
                return millis;
            }
            if(timeUnit ==TimeUnit.SECONDS){
                millis = 1000L * timeout;
                return millis;
            }
        }catch (Exception ignore){
        }
        return millis;
    }
}
