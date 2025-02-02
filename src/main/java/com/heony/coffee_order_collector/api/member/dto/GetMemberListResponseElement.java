package com.heony.coffee_order_collector.api.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import generated.jooq.obj.tables.pojos.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetMemberListResponseElement(

        @Schema(description = "아이디")
        long id,

        @Schema(description = "이름")
        String name
) {

    public static GetMemberListResponseElement from(Member member) {
        return new GetMemberListResponseElement(member.id(), member.name());
    }
}
