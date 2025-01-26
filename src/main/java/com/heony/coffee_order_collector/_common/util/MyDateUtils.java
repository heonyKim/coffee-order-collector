package com.heony.coffee_order_collector._common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Date;

@Slf4j
@Component
public class MyDateUtils {

    public static long convertToGmtPlus9ToMillis(LocalDate dateTime) {
        return dateTime.atStartOfDay(ZoneOffset.ofHours(9)).toEpochSecond() * 1000;
    }
}
