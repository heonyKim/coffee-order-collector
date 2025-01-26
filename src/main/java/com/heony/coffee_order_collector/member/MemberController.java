package com.heony.coffee_order_collector.member;


import com.heony.coffee_order_collector.member.dto.CreateMemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createMember(CreateMemberRequest.Endpoint endpointRequest) {
        memberService.createMember(endpointRequest.toServiceRequest());
        return ResponseEntity.ok().build();
    }

}
