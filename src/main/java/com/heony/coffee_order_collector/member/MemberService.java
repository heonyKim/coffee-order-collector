package com.heony.coffee_order_collector.member;

import com.heony.coffee_order_collector._common.dao.MemberRepository;
import com.heony.coffee_order_collector._common.exception.CustomException;
import com.heony.coffee_order_collector._common.exception.ErrorCodes;
import com.heony.coffee_order_collector.member.dto.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void createMember(CreateMemberRequest.Service serviceRequest) {
        if(memberRepository.existsByName(serviceRequest.name())){
            throw new CustomException(ErrorCodes.NAME_USED_ALREADY);
        }
        memberRepository.insert(serviceRequest.toPojo());
    }
}
