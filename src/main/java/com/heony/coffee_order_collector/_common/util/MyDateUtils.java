package com.heony.coffee_order_collector._common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.*;

@Slf4j
@Component
public class MyDateUtils {

    public static final ZoneId ZONE_GMT_9 = ZoneId.of("GMT+9");

    public static LocalDate getLocalDateByMillis(long requestedAt) {
        Instant instant = Instant.ofEpochMilli(requestedAt);
        return instant.atZone(ZONE_GMT_9).toLocalDate();
    }

    public static long getTodayStartMillisGMT9(LocalDate today){
        today = today==null ? LocalDate.now(ZONE_GMT_9) : today;
        ZonedDateTime startOfToday = today.atStartOfDay(ZONE_GMT_9);
        return startOfToday.toInstant().toEpochMilli();
    }

    public static long getTomorrowStartMillisGMT9(LocalDate today){
        today = today==null ? LocalDate.now(ZONE_GMT_9) : today;
        ZonedDateTime startOfTomorrow = today.plusDays(1).atStartOfDay(ZONE_GMT_9);
        return startOfTomorrow.toInstant().toEpochMilli();
    }
}
