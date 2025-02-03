package com.heony.coffee_order_collector._common.scheduling;

import com.heony.coffee_order_collector._common.dao.MenuRepository;
import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector.api.menu.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * cron은 해도해도 외워지지 않는다..
 * <p>1. {@code @Scheduled} 어노테이션의 {@link Scheduled#zone()} 필드에 {@code "GMT+9"} 값을 넣으면, 한국시간 기준으로 작성 할 수 있음</p>
 *
 * @see <a href="https://dev-bri.tistory.com/4">크론 표현식 (Cron Expression) 정리</a>
 * */

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class UpdatedMenuListTask {

    private final MenuService menuService;
    private final MenuRepository menuRepository;

    @Scheduled(cron = "0 0 8 * * Mon", zone = "GMT+9")
    public void run(){
        menuService.updatedMenuListTask(Corp.MAMMOTH_COFFEE);
    }

}
