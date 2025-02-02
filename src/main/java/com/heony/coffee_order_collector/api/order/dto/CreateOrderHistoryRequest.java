package com.heony.coffee_order_collector.api.order.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.heony.coffee_order_collector._common.enums.Corp;
import generated.jooq.obj.tables.pojos.Member;
import generated.jooq.obj.tables.pojos.OrderHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateOrderHistoryRequest(

        @Schema(description = "회원 아이디")
        Integer memberId,

        @NotNull
        @Schema(description = "브랜드 종류")
        Corp.Brand brand,

        @Schema(description = "메뉴명")
        String menuName
) {

    public OrderHistory pojoToCreate(Member member, String requestedOrigin, long requestedAt){
        return new OrderHistory(
                null,
                member.id(),
                member.name(),
                this.brand.name(),
                StringUtils.isBlank(this.menuName) ? "PASS" : this.menuName,
                requestedAt,
                requestedOrigin,
                null,
                null
        );
    }

    public OrderHistory pojoToUpdate(OrderHistory originOrderHistory, String requestedOrigin, long requestedAt){
        return new OrderHistory(
                originOrderHistory.id(),
                originOrderHistory.memberId(),
                originOrderHistory.memberName(),
                originOrderHistory.brand(),
                StringUtils.isBlank(this.menuName) ? "PASS" : this.menuName,
                originOrderHistory.createdAt(),
                originOrderHistory.createdOrigin(),
                requestedAt,
                requestedOrigin
        );
    }

}
