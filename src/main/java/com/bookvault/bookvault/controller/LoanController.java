package com.bookvault.bookvault.controller;

import com.bookvault.bookvault.dto.LoanDTO;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseDTO borrowBook(@RequestBody LoanDTO dto) {
        return loanService.borrowBook(dto);
    }

    @PutMapping("/{id}/return")
    public ResponseDTO returnBook(@PathVariable String id) {
        return loanService.returnBook(id);
    }

    @GetMapping("/overdue")
    public ResponseDTO getOverdueLoans(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "dueDate") String sortBy,
                                       @RequestParam(defaultValue = "asc") String direction) {
        return loanService.getOverdueLoans(page, size, sortBy, direction);
    }
}
