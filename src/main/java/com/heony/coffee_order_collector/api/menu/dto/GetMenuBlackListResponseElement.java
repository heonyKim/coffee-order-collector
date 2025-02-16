package com.heony.coffee_order_collector.api.menu.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import generated.jooq.obj.tables.pojos.MenuBlack;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetMenuBlackListResponseElement(

        @Schema(description = "메뉴명")
        String name

) {
    public static GetMenuBlackListResponseElement from(MenuBlack menuBlack){
        return new GetMenuBlackListResponseElement(menuBlack.name());
    }
}
