package com.bookvault.bookvault.serviceImpl;

import com.bookvault.bookvault.dto.LoanDTO;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.entity.Book;
import com.bookvault.bookvault.entity.Loan;
import com.bookvault.bookvault.entity.Member;
import com.bookvault.bookvault.enums.LoanStatus;
import com.bookvault.bookvault.enums.MembershipStatus;
import com.bookvault.bookvault.repository.BookRepository;
import com.bookvault.bookvault.repository.LoanRepository;
import com.bookvault.bookvault.repository.MemberRepository;
import com.bookvault.bookvault.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {


        private final LoanRepository loanRepository;
        private final BookRepository bookRepository;
        private final MemberRepository memberRepository;

        @Override
        public ResponseDTO borrowBook(LoanDTO dto) {

            // 1. Fetch Book
            Book book = bookRepository.findById(UUID.fromString(dto.getBookId()))
                    .orElseThrow(() -> new RuntimeException("BOOK_NOT_FOUND"));

            // 2. Fetch Member
            Member member = memberRepository.findById(UUID.fromString(dto.getMemberId()))
                    .orElseThrow(() -> new RuntimeException("MEMBER_NOT_FOUND"));

            // 3. Validate book availability
            if (book.getAvailableCopies() <= 0) {
                throw new RuntimeException("BOOK_NOT_AVAILABLE");
            }

            // 4. Validate member status
            if (member.getMembershipStatus() == MembershipStatus.SUSPENDED) {
                throw new RuntimeException("MEMBER_SUSPENDED");
            }

            // 5. Decrement available copies
            book.setAvailableCopies(book.getAvailableCopies() - 1);

            // 6. Create Loan
            Loan loan = new Loan();
            loan.setBook(book);
            loan.setMember(member);
            loan.setBorrowedAt(LocalDateTime.now());
            loan.setDueDate(LocalDateTime.now().plusDays(14));
            loan.setStatus(LoanStatus.ACTIVE);

            loanRepository.save(loan);
            bookRepository.save(book);

            return new ResponseDTO(
                    "SUCCESS",
                    "Book borrowed successfully",
                    loan.getId()
            );
        }
}
