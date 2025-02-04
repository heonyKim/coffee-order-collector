package com.heony.coffee_order_collector.api.menu;

import com.heony.coffee_order_collector._common.dao.MenuRepository;
import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector._common.enums.StoreType;
import com.heony.coffee_order_collector.infra.mammoth.MammothService;
import generated.jooq.obj.tables.pojos.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MammothService mammothService;
    private final MenuRepository menuRepository;

    @Transactional
    public void updatedMenuListTask(Corp corp){
        switch (corp){
            case MAMMOTH_COFFEE -> {
                menuRepository.deleteAllByBrand(Corp.Brand.MAMMOTH_EXPRESS);
                menuRepository.deleteAllByBrand(Corp.Brand.MAMMOTH_COFFEE);
                this.crawlingMenus(Corp.MAMMOTH_COFFEE);
            }
            case null, default -> {}
        }
    }

    @Transactional
    public void crawlingMenus(Corp corp) {
        long requestedAt = System.currentTimeMillis();

        List<Menu> menus;
        switch (corp) {
            case MAMMOTH_COFFEE -> {

                menus = mammothService.crawlingMenus(StoreType.MAMMOTH_COFFEE_NEW, requestedAt);
                if (menus != null) menuRepository.insert(menus);

                menus = mammothService.crawlingMenus(StoreType.MAMMOTH_COFFEE, requestedAt);
                if (menus != null) menuRepository.insert(menus);

                menus = mammothService.crawlingMenus(StoreType.MAMMOTH_EXPRESS_NEW, requestedAt);
                if (menus != null) menuRepository.insert(menus);

                menus = mammothService.crawlingMenus(StoreType.MAMMOTH_EXPRESS, requestedAt);
                if (menus != null) menuRepository.insert(menus);

            }
            case null, default -> {}
        }

    }

    public List<Menu> getMenuList(Corp.Brand corpBrand){
        return menuRepository.findByBrand(corpBrand);
    }

    public List<Menu> getFavoriteMenuList(Corp.Brand corpBrand){
        return switch (corpBrand){
            case MAMMOTH_COFFEE -> List.of();
            case MAMMOTH_EXPRESS -> {
                List<Menu> byBrandAndNames = menuRepository.findByBrandAndNames(
                        Corp.Brand.MAMMOTH_EXPRESS,
                        List.of("아메리카노", "카페 라떼", "바닐라 라떼", "초코 라떼", "토피넛 라떼", "지리산 청매실티")
                );
                byBrandAndNames.sort((o1, o2) -> setFirstTo("아메리카노", o1, o2));
                yield byBrandAndNames;
            }
            case null, default -> List.of();
        };
    }

    private static int setFirstTo(String menuName, Menu o1, Menu o2) {
        if(menuName.equals(o1.name()) && !menuName.equals(o2.name())){
            return -1;
        }else if (!menuName.equals(o1.name()) && menuName.equals(o2.name())){
            return 1;
        }else{
            return 0;
        }
    }


}
