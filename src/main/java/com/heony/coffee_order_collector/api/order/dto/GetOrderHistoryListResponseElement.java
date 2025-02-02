package com.heony.coffee_order_collector.api.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import generated.jooq.obj.tables.pojos.OrderHistory;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetOrderHistoryListResponseElement(
        Integer id,
        String memberName,
        String menuName
) {

    public static GetOrderHistoryListResponseElement from(OrderHistory orderHistory) {
        return new GetOrderHistoryListResponseElement(
                orderHistory.id(),
                orderHistory.memberName(),
                orderHistory.menuName()
        );
    }
}
