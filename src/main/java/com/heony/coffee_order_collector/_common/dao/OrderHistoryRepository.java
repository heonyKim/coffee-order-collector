package com.heony.coffee_order_collector._common.dao;

import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector._common.enums.Corp.Brand;
import com.heony.coffee_order_collector._common.util.MyDateUtils;
import generated.jooq.obj.tables.daos.MenuDao;
import generated.jooq.obj.tables.daos.OrderHistoryDao;
import generated.jooq.obj.tables.pojos.Menu;
import generated.jooq.obj.tables.pojos.OrderHistory;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository
public class OrderHistoryRepository extends OrderHistoryDao {

    private static final generated.jooq.obj.tables.OrderHistory ORDER_HISTORY = generated.jooq.obj.tables.OrderHistory.ORDER_HISTORY;

    private final DSLContext dsl;

    public OrderHistoryRepository(DSLContext dsl) {
        super(dsl.configuration());
        this.dsl = dsl;
    }

    public OrderHistory getTodayOrderHistory(Integer memberId, Corp.Brand brand, LocalDate date){
        long startedAt = MyDateUtils.getTodayStartMillisGMT9(date);
        long endedAt = MyDateUtils.getTomorrowStartMillisGMT9(date);

        return dsl.selectFrom(ORDER_HISTORY)
                .where(ORDER_HISTORY.MEMBER_ID.eq(memberId))
                .and(ORDER_HISTORY.BRAND.eq(brand.name()))
                .and(ORDER_HISTORY.CREATED_AT.ge(startedAt)
                        .and(ORDER_HISTORY.CREATED_AT.lt(endedAt))
                )
                .fetchOneInto(OrderHistory.class);
    }

    public List<OrderHistory> getOrderHistoryList(
            LocalDate startDate, LocalDate endDate, Integer memberId,Corp.Brand brand, String menuName, String memberName
    ){
        Condition condition = DSL.one().eq(DSL.one());
        if(startDate != null){
            condition = condition.and(ORDER_HISTORY.CREATED_AT.ge(MyDateUtils.getTodayStartMillisGMT9(startDate)));
        }
        if(endDate != null){
            condition = condition.and(ORDER_HISTORY.CREATED_AT.lt(MyDateUtils.getTomorrowStartMillisGMT9(endDate)));
        }
        if(memberId != null){
            condition = condition.and(ORDER_HISTORY.MEMBER_ID.eq(memberId));
        }
        if(brand != null){
            condition = condition.and(ORDER_HISTORY.BRAND.eq(brand.name()));
        }
        if(menuName != null){
            condition = condition.and(ORDER_HISTORY.MENU_NAME.contains(menuName));
        }
        if(memberName != null){
            condition = condition.and(ORDER_HISTORY.MEMBER_NAME.contains(memberName));
        }
        return dsl.selectFrom(ORDER_HISTORY)
                .where(condition)
                .fetchInto(OrderHistory.class);
    }

}
