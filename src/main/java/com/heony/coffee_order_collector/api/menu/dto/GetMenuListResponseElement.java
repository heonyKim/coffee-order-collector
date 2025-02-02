package com.heony.coffee_order_collector.api.menu.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.heony.coffee_order_collector._common.enums.*;
import generated.jooq.obj.tables.pojos.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetMenuListResponseElement(

        @Schema(description = "메뉴 아이디")
        Integer id,

        @Schema(description = "메뉴명")
        String name,

        @Schema(description = "브랜드 종류")
        Corp.Brand brand,

        @Schema(description = "메뉴 카테고리")
        MenuCategory category,

        @Schema(description = "메뉴 카테고리 한글명")
        String categoryName,

        @Schema(description = "메뉴 온도")
        List<MenuTemperature> temperatures,

        @Schema(description = "메뉴 사이즈")
        List<String> menuSizes,

        @Schema(description = "메뉴 이미지 URL")
        String imageUrl
) {
    public static GetMenuListResponseElement from(Menu menu){
        MenuCategory menuCategory = MenuCategory.valueOf(menu.category());
        String categoryName = menuCategory.categoryName();

        return new GetMenuListResponseElement(
                menu.id(),
                menu.name(),
                Corp.Brand.valueOf(menu.brand()),
                menuCategory,
                categoryName,
                ObjectUtils.isEmpty(menu.temperatures()) ? null : Arrays.stream(menu.temperatures().split(",")).map(MenuTemperature::valueOf).toList(),
                ObjectUtils.isEmpty(menu.temperatures()) ? null : MenuSize.getMammothSizesString(),
                menu.imageUrl()
        );
    }
}
