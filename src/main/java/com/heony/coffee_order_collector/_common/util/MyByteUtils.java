package com.heony.coffee_order_collector._common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class MyByteUtils {

    public static byte[] randomUUIDBytes(){
        return UUID.randomUUID().toString().getBytes();
    }

    public static UUID uuidByBytes(byte[] bytes){
        return UUID.nameUUIDFromBytes(bytes);
    }
}
