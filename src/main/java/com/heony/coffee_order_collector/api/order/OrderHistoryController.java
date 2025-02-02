package com.heony.coffee_order_collector.api.order;

import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector._common.enums.StoreType;
import com.heony.coffee_order_collector.api.order.dto.CreateOrderHistoryRequest;
import com.heony.coffee_order_collector.api.order.dto.GetOrderHistoryListResponseElement;
import generated.jooq.obj.tables.pojos.OrderHistory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/order-histories")
@RequiredArgsConstructor
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @PostMapping
    public ResponseEntity<Void> createOrderHistory(@RequestBody CreateOrderHistoryRequest requestDto, HttpServletRequest request) {
        orderHistoryService.createOrderHistory(requestDto, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GetOrderHistoryListResponseElement>> getOrderHistoryList(
            @RequestParam(required = false, name = "start_date") LocalDate startDate,
            @RequestParam(required = false, name = "end_date") LocalDate endDate,
            @RequestParam(required = false, name = "member_id") Integer memberId,
            @RequestParam(required = false, name = "brand") Corp.Brand brand,
            @RequestParam(required = false, name = "menu_name") String menuName,
            @RequestParam(required = false, name = "member_name") String memberName
    ) {
        List<OrderHistory> orderHistoryList = orderHistoryService.getOrderHistoryList(startDate, endDate, memberId, brand, menuName, memberName);
        return ResponseEntity.ok(orderHistoryList.stream().map(GetOrderHistoryListResponseElement::from).toList());
    }

    @GetMapping("/not-yet")
    public ResponseEntity<List<String>> getOrderNotYetMemberList(
            @RequestParam(required = false, name = "start_date") LocalDate startDate,
            @RequestParam(required = false, name = "end_date") LocalDate endDate,
            @RequestParam(required = false, name = "brand") Corp.Brand brand
    ){
        List<String> memberNameList = orderHistoryService.getOrderNotYetMemberList(startDate, endDate, brand);
        return ResponseEntity.ok(memberNameList);
    }
}
