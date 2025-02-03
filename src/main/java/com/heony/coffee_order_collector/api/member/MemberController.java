package com.heony.coffee_order_collector.api.member;


import com.heony.coffee_order_collector.api.member.dto.CreateMemberRequest;
import com.heony.coffee_order_collector.api.member.dto.GetMemberListResponseElement;
import generated.jooq.obj.tables.pojos.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createMember(
            @RequestBody CreateMemberRequest.Endpoint endpointRequest
    ) {
        memberService.createMember(endpointRequest.toServiceRequest());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GetMemberListResponseElement>> getMemberList() {
        List<Member> memberList = memberService.getMemberList();
        return ResponseEntity.ok(memberList.stream().map(GetMemberListResponseElement::from).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Integer id){
        memberService.deleteMember(id);
        return ResponseEntity.ok().build();
    }


}
