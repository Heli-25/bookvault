package com.bookvault.bookvault.controller;

import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.service.LoanService;
import com.bookvault.bookvault.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final LoanService loanService;
    private final MemberService memberService;

    @GetMapping("/{id}/loans")
    public ResponseDTO getMemberLoans(@PathVariable String id) {
        return loanService.getLoansByMember(id);
    }

    @GetMapping("/search")
    public ResponseDTO searchMembers(@RequestParam("q") String query) {
        return memberService.searchMembers(query);
    }
}
