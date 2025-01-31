package com.heony.coffee_order_collector._common.dao;

import com.heony.coffee_order_collector._common.enums.StoreType;
import generated.jooq.obj.tables.daos.MenuDao;
import generated.jooq.obj.tables.pojos.Menu;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MenuRepository extends MenuDao {

    private static final generated.jooq.obj.tables.Menu MENU = generated.jooq.obj.tables.Menu.MENU;

    private final DSLContext dsl;

    public MenuRepository(DSLContext dsl) {
        super(dsl.configuration());
        this.dsl = dsl;
    }

    public List<Menu> findByStoreTypes(List<StoreType> storeTypes) {
        return dsl.selectFrom(MENU)
                .where(MENU.STORE_TYPE.in(storeTypes))
                .fetchInto(Menu.class);
    }
}
