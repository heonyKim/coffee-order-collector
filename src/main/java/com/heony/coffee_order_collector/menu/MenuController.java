package com.heony.coffee_order_collector.menu;

import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector.menu.dto.GetMenuListResponseElement;
import generated.jooq.obj.tables.pojos.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/crawling/{corp}")
    public ResponseEntity<Void> crawlingMenus(@PathVariable Corp corp) {
        menuService.crawlingMenus(corp);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{corpBrand}")
    public ResponseEntity<List<GetMenuListResponseElement>> getMenuList(
            @PathVariable Corp.Brand corpBrand
    ) {
        List<Menu> menuList = menuService.getMenuList(corpBrand);
        return ResponseEntity.ok(menuList.stream().map(GetMenuListResponseElement::from).toList());
    }

}
