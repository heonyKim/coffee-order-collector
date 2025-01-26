package com.heony.coffee_order_collector._common.enums;

import java.util.List;

public enum MenuSize {

    MAMMOTH_SIZE_S("S", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS}),
    MAMMOTH_SIZE_M("M", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS}),
    MAMMOTH_SIZE_L("L", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS}),
    ;

    private final String menuSize;
    private final StoreType[] storeTypes;

    public static List<String> getMammothSizes() {
        return List.of(MAMMOTH_SIZE_S.menuSize(), MAMMOTH_SIZE_M.menuSize(), MAMMOTH_SIZE_L.menuSize());
    }

    MenuSize(String menuSize, StoreType[] storeTypes) {
        this.menuSize = menuSize;
        this.storeTypes = storeTypes;
    }

    public String menuSize() {
        return menuSize;
    }

    public StoreType[] storeTypes() {
        return storeTypes;
    }
}
