package com.heony.coffee_order_collector._common.dao;

import generated.jooq.obj.tables.daos.MemberDao;
import generated.jooq.obj.tables.pojos.Member;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * Retrieve all members sorted by name in ascending order.
     *
     * @return A list of all members sorted by name in ascending order.
     */
    public List<Member> findAllByNameAsc() {
        return dsl.selectFrom(MEMBER)
                .orderBy(MEMBER.NAME.asc())
                .fetchInto(Member.class);
    }

    public List<String> findAllNameByNameAsc() {
        return dsl.select(MEMBER.NAME)
                .from(MEMBER)
                .orderBy(MEMBER.NAME.asc())
                .fetchInto(String.class);
    }



}
