package com.bookvault.bookvault.service;

import com.bookvault.bookvault.dto.LoanDTO;
import com.bookvault.bookvault.dto.ResponseDTO;

public interface LoanService {
    ResponseDTO borrowBook(LoanDTO dto);

    ResponseDTO returnBook(String loanId);

    ResponseDTO getOverdueLoans(int page, int size, String sortBy, String direction);

    ResponseDTO getLoansByMember(String memberId);
}
