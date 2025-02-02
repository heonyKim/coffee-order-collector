package com.heony.coffee_order_collector._common.enums;

import java.util.Arrays;

public enum MenuCategory {

    MAMMOTH_32OZ("32oz", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW}),
    MAMMOTH_COFFEE("커피", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_EXPRESS_NEW}),
    MAMMOTH_COLD_BREW("콜드브루", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_EXPRESS_NEW}),
    MAMMOTH_NON_COFFEE("논커피", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_EXPRESS_NEW}),
    MAMMOTH_TEA_ADE("티·에이드", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_EXPRESS_NEW}),
    MAMMOTH_MIXED("프라페·블렌디드", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_EXPRESS_NEW}),
    MAMMOTH_FOOD("푸드", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_EXPRESS_NEW}),
    MAMMOTH_RTD("RTD", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_EXPRESS_NEW}),
    MAMMOTH_MD("MD", new StoreType[]{StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_EXPRESS_NEW}),
    MAMMOTH_NEW("신메뉴", new StoreType[]{StoreType.MAMMOTH_COFFEE_NEW, StoreType.MAMMOTH_EXPRESS_NEW}),
    ;

    private final String categoryName;
    private final StoreType[] storeTypes;

    public static MenuCategory getByCategoryNameAndStoreType(String categoryName, StoreType storeType) {
        for (MenuCategory menuCategory : MenuCategory.values()) {
            if (menuCategory.categoryName().equals(categoryName) && Arrays.stream(menuCategory.storeTypes()).toList().contains(storeType)) {
                return menuCategory;
            }
        }
        return null;
    }

    MenuCategory(String categoryName, StoreType[] storeTypes) {
        this.categoryName = categoryName;
        this.storeTypes = storeTypes;
    }

    public String categoryName() {
        return categoryName;
    }

    public StoreType[] storeTypes() {
        return storeTypes;
    }
}
