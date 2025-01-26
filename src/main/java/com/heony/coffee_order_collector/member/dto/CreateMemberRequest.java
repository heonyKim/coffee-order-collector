package com.heony.coffee_order_collector.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.heony.coffee_order_collector._common.util.MyByteUtils;
import com.heony.coffee_order_collector._common.util.MyDateUtils;
import generated.jooq.obj.tables.pojos.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateMemberRequest(

) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Schema(name = "CreateMemberRequestEndpoint")
    public record Endpoint(

            @NotEmpty
            String name
    ){
        public CreateMemberRequest.Service toServiceRequest(){
            return new CreateMemberRequest.Service(
                    this.name.strip(),
                    System.currentTimeMillis()
            );
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Schema(name = "CreateMemberRequestService")
    public record Service(
            String name,
            long createdAt
    ){
        public Member toPojo(){
            return new Member(
                    null,
                    this.name,
                    this.createdAt
            );
        }
    }
}
