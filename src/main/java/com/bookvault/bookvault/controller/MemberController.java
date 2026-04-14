package com.bookvault.bookvault.controller;

import com.bookvault.bookvault.dto.MemberDTO;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.service.LoanService;
import com.bookvault.bookvault.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final LoanService loanService;
    private final MemberService memberService;

    @GetMapping
    public ResponseDTO getMembers() {
        return memberService.getMembers();
    }

    @GetMapping("/{id}")
    public ResponseDTO getMemberById(@PathVariable String id) {
        return memberService.getMemberById(id);
    }

    @PostMapping
    public ResponseDTO createMember(@Valid @RequestBody MemberDTO dto) {
        return memberService.createMember(dto);
    }

    @PutMapping("/{id}")
    public ResponseDTO updateMember(@PathVariable String id, @Valid @RequestBody MemberDTO dto) {
        return memberService.updateMember(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteMember(@PathVariable String id) {
        return memberService.deleteMember(id);
    }

    @GetMapping("/{id}/loans")
    public ResponseDTO getMemberLoans(@PathVariable String id) {
        return loanService.getLoansByMember(id);
    }

    @GetMapping("/search")
    public ResponseDTO searchMembers(@RequestParam("q") String query) {
        return memberService.searchMembers(query);
    }
}
