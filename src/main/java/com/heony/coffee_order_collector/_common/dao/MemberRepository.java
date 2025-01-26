package com.heony.coffee_order_collector._common.dao;

import generated.jooq.obj.tables.daos.MemberDao;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MemberRepository extends MemberDao {

    private static final generated.jooq.obj.tables.Member MEMBER = generated.jooq.obj.tables.Member.MEMBER;

    private final DSLContext dsl;

    public MemberRepository(DSLContext dsl) {
        super(dsl.configuration());
        this.dsl = dsl;
    }

    public boolean existsByName(String name) {
        return dsl.fetchExists(
                dsl.select()
                        .from(MEMBER)
                        .where(MEMBER.NAME.eq(name))
        );
    }


}
