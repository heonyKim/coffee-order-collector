package com.heony.coffee_order_collector._common.enums;

public enum MammothMenuTemperature {
    HOT(MenuTemperature.HOT),
    ICE(MenuTemperature.ICE),
    ;

    public MenuTemperature menuTemperature() {
        return menuTemperature;
    }

    private MenuTemperature menuTemperature;

    MammothMenuTemperature(MenuTemperature menuTemperature) {
        this.menuTemperature = menuTemperature;
    }
}
