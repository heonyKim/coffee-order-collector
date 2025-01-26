package com.heony.coffee_order_collector._common.dao;

import generated.jooq.obj.tables.daos.MenuDao;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MenuRepository extends MenuDao {

    private static final generated.jooq.obj.tables.Menu MENU = generated.jooq.obj.tables.Menu.MENU;

    private final DSLContext dsl;

    public MenuRepository(DSLContext dsl) {
        super(dsl.configuration());
        this.dsl = dsl;
    }
}
