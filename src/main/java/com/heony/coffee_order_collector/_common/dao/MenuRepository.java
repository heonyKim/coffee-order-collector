package com.heony.coffee_order_collector._common.dao;

import com.heony.coffee_order_collector._common.enums.Corp;
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

    public List<Menu> findByBrand(Corp.Brand brand) {
        return dsl.selectDistinct().from(MENU)
                .where(MENU.BRAND.eq(brand.name()))
                .fetchInto(Menu.class);
    }


    public List<Menu> findByBrandAndNames(Corp.Brand brand, List<String> names){
        return dsl.selectDistinct().from(MENU)
                .where(MENU.BRAND.eq(brand.name()))
                .and(MENU.NAME.in(names))
                .fetchInto(Menu.class);
    }

    public void deleteAllByBrand(Corp.Brand brand){
        dsl.deleteFrom(MENU).where(MENU.BRAND.eq(brand.name())).execute();
    }
}
