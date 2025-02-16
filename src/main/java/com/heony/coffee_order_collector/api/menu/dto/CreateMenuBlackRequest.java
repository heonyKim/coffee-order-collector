package com.heony.coffee_order_collector.api.menu.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector._common.util.MyNetworkUtils;
import generated.jooq.obj.tables.pojos.Member;
import generated.jooq.obj.tables.pojos.MenuBlack;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateMenuBlackRequest(

) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Schema(name = "CreateMenuBlackRequestEndpoint")
    public record Endpoint(

            @Schema(description = "이름")
            @NotEmpty
            String name
    ){
        public CreateMenuBlackRequest.Service toServiceRequest(){
            return new CreateMenuBlackRequest.Service(
                    this.name.strip(),
                    System.currentTimeMillis()
            );
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Schema(name = "CreateMenuBlackRequestService")
    public record Service(
            String name,
            long createdAt
    ){
        public MenuBlack toPojo(Corp.Brand brand, String createdOrigin){
            return new MenuBlack(
                    null,
                    brand.name(),
                    this.createdAt,
                    createdOrigin,
                    this.name
            );
        }
    }
}
