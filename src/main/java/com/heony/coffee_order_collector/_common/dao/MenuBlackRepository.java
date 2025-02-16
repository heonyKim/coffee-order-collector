package com.heony.coffee_order_collector._common.dao;

import com.heony.coffee_order_collector._common.enums.Corp;
import generated.jooq.obj.tables.daos.MenuBlackDao;
import generated.jooq.obj.tables.daos.MenuDao;
import generated.jooq.obj.tables.pojos.Menu;
import generated.jooq.obj.tables.pojos.MenuBlack;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class MenuBlackRepository extends MenuBlackDao {

    private static final generated.jooq.obj.tables.MenuBlack MENU_BLACK = generated.jooq.obj.tables.MenuBlack.MENU_BLACK;

    private final DSLContext dsl;

    public MenuBlackRepository(DSLContext dsl) {
        super(dsl.configuration());
        this.dsl = dsl;
    }

    public List<MenuBlack> findByBrand(Corp.Brand brand) {
        return dsl.selectDistinct().from(MENU_BLACK)
                .where(MENU_BLACK.BRAND.eq(brand.name()))
                .orderBy(MENU_BLACK.NAME.asc())
                .fetchInto(MenuBlack.class);
    }


    public List<MenuBlack> findByBrandAndNames(Corp.Brand brand, List<String> names){
        return dsl.selectDistinct().from(MENU_BLACK)
                .where(MENU_BLACK.BRAND.eq(brand.name()))
                .and(MENU_BLACK.NAME.in(names))
                .fetchInto(MenuBlack.class);
    }

    public void deleteAllByBrand(Corp.Brand brand){
        dsl.deleteFrom(MENU_BLACK).where(MENU_BLACK.BRAND.eq(brand.name())).execute();
    }

    public void deleteByBrandAndName(Corp.Brand brand, String name) {
        dsl.deleteFrom(MENU_BLACK)
                .where(MENU_BLACK.BRAND.eq(brand.name()))
                .and(MENU_BLACK.NAME.eq(name))
                .execute();
    }

    public boolean existsByBrandAndName(Corp.Brand brand, String name) {
        return dsl.fetchExists(
                dsl.select().from(MENU_BLACK)
                        .where(MENU_BLACK.BRAND.eq(brand.name()))
                        .and(MENU_BLACK.NAME.eq(name))
        );
    }
}
