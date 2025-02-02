package com.heony.coffee_order_collector.api.order;

import com.heony.coffee_order_collector._common.GlobalVariables;
import com.heony.coffee_order_collector._common.dao.MemberRepository;
import com.heony.coffee_order_collector._common.dao.OrderHistoryRepository;
import com.heony.coffee_order_collector._common.enums.Corp;
import com.heony.coffee_order_collector._common.enums.StoreType;
import com.heony.coffee_order_collector._common.exception.CustomException;
import com.heony.coffee_order_collector._common.exception.ErrorCodes;
import com.heony.coffee_order_collector._common.util.MyDateUtils;
import com.heony.coffee_order_collector._common.util.MyNetworkUtils;
import com.heony.coffee_order_collector.api.order.dto.CreateOrderHistoryRequest;
import generated.jooq.obj.tables.pojos.Member;
import generated.jooq.obj.tables.pojos.OrderHistory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    @Value("${app.update-available.enabled:true}")
    private boolean APP_UPDATE_AVAILABLE_ENABLED;

    private final OrderHistoryRepository orderHistoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createOrderHistory(CreateOrderHistoryRequest requestDto, HttpServletRequest request) {
        long requestedAt = System.currentTimeMillis();

        Member member = memberRepository.findById(requestDto.memberId());
        if (member == null) throw new CustomException(ErrorCodes.MEMBER_NOT_FOUND);

        //금일 주문내역이 존재하는지 확인
        OrderHistory todayOrderHistory = orderHistoryRepository.getTodayOrderHistory(
                member.id(),
                requestDto.brand(),
                MyDateUtils.getLocalDateByMillis(requestedAt)
        );

        boolean beCreated = true;
        OrderHistory orderHistory;
        if(todayOrderHistory == null){
            //새로운 주문내역 기록
            orderHistory = requestDto.pojoToCreate(
                    member,
                    MyNetworkUtils.getClientIp(request),
                    requestedAt
            );
        }else{
            //기존 히스트리 수정
            if(this.APP_UPDATE_AVAILABLE_ENABLED){
                //기존 주문내역 수정 가능
                if(todayOrderHistory.createdAt()+GlobalVariables.APP_UPDATE_AVAILABLE_TIME_MILLIES>=requestedAt){
                    // 시간초과 하지 않아서 수정가능
                    beCreated = false;
                    orderHistory = requestDto.pojoToUpdate(
                            todayOrderHistory,
                            MyNetworkUtils.getClientIp(request),
                            requestedAt
                    );

                }else{
                    // 시간초과 하였으므로 수정불가
                    throw new CustomException(ErrorCodes.UPDATE_AVAILABLE_TIMEOUT);
                }

            }else{
                //기존 주문내역 수정 불가
                throw new CustomException(ErrorCodes.UPDATE_AVALIABLE_DISABLE);
            }
        }

        if(beCreated){ //생성
            orderHistoryRepository.insert(orderHistory);
        }else{  //수정
            orderHistoryRepository.update(orderHistory);
        }

    }

    public List<OrderHistory> getOrderHistoryList(
            LocalDate startDate, LocalDate endDate,
            Integer memberId, Corp.Brand brand,
            String menuName, String memberName
    ){
        return orderHistoryRepository.getOrderHistoryList(
                startDate, endDate, memberId, brand, menuName, memberName
        );
    }

    public List<String> getOrderNotYetMemberList(
            LocalDate startDate, LocalDate endDate, Corp.Brand brand
    ){
        List<String> memberNameList = memberRepository.findAllNameByNameAsc();
        List<OrderHistory> orderHistoryList = this.getOrderHistoryList(startDate, endDate, null, brand, null, null);

        return memberNameList.stream()
                .filter(memberName -> orderHistoryList.stream().noneMatch(orderHistory -> orderHistory.memberName().equals(memberName)))
                .toList();
    }


}
