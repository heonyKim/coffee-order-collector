package com.heony.coffee_order_collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoffeeOrderCollectorApplication {

    public static void main(String[] args) {
        System.setProperty("org.jooq.no-tips", "true");
        System.setProperty("org.jooq.no-logo", "true");
        SpringApplication.run(CoffeeOrderCollectorApplication.class, args);
    }

}
