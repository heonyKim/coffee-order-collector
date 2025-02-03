package com.heony.coffee_order_collector.api.member;

import com.heony.coffee_order_collector._common.dao.MemberRepository;
import com.heony.coffee_order_collector._common.exception.CustomException;
import com.heony.coffee_order_collector._common.exception.ErrorCodes;
import com.heony.coffee_order_collector._common.util.MyDateUtils;
import com.heony.coffee_order_collector.api.member.dto.CreateMemberRequest;
import generated.jooq.obj.tables.pojos.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

    public List<Member> getMemberList(){
        return memberRepository.findAllByNameAsc();
    }

    @Transactional
    public void deleteMember(Integer id){
        memberRepository.deleteById(id);
    }
}
