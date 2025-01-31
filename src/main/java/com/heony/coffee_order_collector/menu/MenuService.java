package com.heony.coffee_order_collector.menu;

import com.heony.coffee_order_collector._common.dao.MenuRepository;
import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector._common.enums.StoreType;
import com.heony.coffee_order_collector.infra.mammoth.MammothService;
import generated.jooq.obj.tables.pojos.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MammothService mammothService;
    private final MenuRepository menuRepository;

    @Transactional
    public void crawlingMenus(Corp corp) {
        long requestedAt = System.currentTimeMillis();

        List<Menu> menus;
        switch (corp) {
            case MAMMOTH_COFFEE -> {
                menus = mammothService.crawlingMenus(StoreType.MAMMOTH_COFFEE, requestedAt);
                if (menus != null) menuRepository.insert(menus);

                menus = mammothService.crawlingMenus(StoreType.MAMMOTH_COFFEE_NEW, requestedAt);
                if (menus != null) menuRepository.insert(menus);

                menus = mammothService.crawlingMenus(StoreType.MAMMOTH_EXPRESS, requestedAt);
                if (menus != null) menuRepository.insert(menus);
            }
            case null, default -> {}
        }

    }

    public List<Menu> getMenuList(Corp.Brand corpBrand){
        List<StoreType> storeTypes = switch (corpBrand){
            case MAMMOTH_COFFEE -> List.of(StoreType.MAMMOTH_COFFEE, StoreType.MAMMOTH_COFFEE_NEW);
            case MAMMOTH_EXPRESS -> List.of(StoreType.MAMMOTH_EXPRESS, StoreType.MAMMOTH_COFFEE_NEW);
            case null, default -> null;
        };
        if(storeTypes == null) new ArrayList<>();
        return menuRepository.findByStoreTypes(storeTypes);
    }

}
