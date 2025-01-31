package com.heony.coffee_order_collector.menu.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.heony.coffee_order_collector._common.enums.MenuCategory;
import com.heony.coffee_order_collector._common.enums.MenuSize;
import com.heony.coffee_order_collector._common.enums.MenuTemperature;
import generated.jooq.obj.tables.pojos.Menu;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetMenuListResponseElement(
        String name,
        MenuCategory category,
        String categoryName,
        List<MenuTemperature> temperatures,
        List<String> menuSizes,
        String imageUrl
) {
    public static GetMenuListResponseElement from(Menu menu){
        MenuCategory menuCategory = MenuCategory.valueOf(menu.category());
        String categoryName = menuCategory.categoryName();

        return new GetMenuListResponseElement(
                menu.name(),
                menuCategory,
                categoryName,
                ObjectUtils.isEmpty(menu.temperatures()) ? null : Arrays.stream(menu.temperatures().split(",")).map(MenuTemperature::valueOf).toList(),
                ObjectUtils.isEmpty(menu.temperatures()) ? null : MenuSize.getMammothSizesString(),
                menu.imageUrl()
        );
    }
}
