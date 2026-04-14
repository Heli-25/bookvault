package com.bookvault.bookvault.controller;

import com.bookvault.bookvault.dto.LoanDTO;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseDTO borrowBook(@RequestBody LoanDTO dto) {
        return loanService.borrowBook(dto);
    }
}
