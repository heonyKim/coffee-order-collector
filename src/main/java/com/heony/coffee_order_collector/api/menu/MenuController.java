package com.heony.coffee_order_collector.api.menu;

import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector.api.menu.dto.CreateMenuBlackRequest;
import com.heony.coffee_order_collector.api.menu.dto.GetMenuBlackListResponseElement;
import com.heony.coffee_order_collector.api.menu.dto.GetMenuListResponseElement;
import generated.jooq.obj.tables.pojos.Menu;
import generated.jooq.obj.tables.pojos.MenuBlack;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/update/menu/manual/{corp}")
    public ResponseEntity<Void> updatedMenuListTask(@PathVariable Corp corp) {
        menuService.updatedMenuListTask(corp);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{corpBrand}")
    public ResponseEntity<List<GetMenuListResponseElement>> getMenuList(
            @PathVariable Corp.Brand corpBrand
    ) {
        List<Menu> menuList = menuService.getMenuList(corpBrand);
        return ResponseEntity.ok(menuList.stream().map(GetMenuListResponseElement::from).toList());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/blacklist/{corpBrand}")
    public ResponseEntity<List<GetMenuBlackListResponseElement>> getMenuBlackList(
            @PathVariable Corp.Brand corpBrand
    ){
        List<MenuBlack> menuBlackList = menuService.getMenuBlackList(corpBrand);
        return ResponseEntity.ok(menuBlackList.stream().map(GetMenuBlackListResponseElement::from).toList());
    }

    @DeleteMapping("/blacklist/{corpBrand}/{blackName}")
    public ResponseEntity<Void> deleteMenuBlack(@PathVariable Corp.Brand corpBrand, @PathVariable String blackName){
        menuService.deleteMenuBlack(corpBrand, blackName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/blacklist/{corpBrand}")
    public ResponseEntity<Void> createMenuBlack(
            @PathVariable Corp.Brand corpBrand,
            @RequestBody CreateMenuBlackRequest.Endpoint endpointRequest,
            HttpServletRequest request
    ) {
        menuService.createMenuBlack(corpBrand, endpointRequest.toServiceRequest(), request);
        return ResponseEntity.ok().build();
    }

}
