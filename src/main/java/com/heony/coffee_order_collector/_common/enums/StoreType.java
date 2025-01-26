package com.heony.coffee_order_collector._common.enums;

public enum StoreType {

    MAMMOTH_COFFEE("매머드커피"),
    MAMMOTH_COFFEE_NEW("매머드커피 신메뉴"),
    MAMMOTH_EXPRESS("매머드 익스프레스"),
    ;

    public String storeName() {
        return storeName;
    }

    private final String storeName;

    StoreType(String storeName) {
        this.storeName = storeName;
    }

}
