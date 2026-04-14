package com.bookvault.bookvault.serviceImpl;

import com.bookvault.bookvault.dto.LoanDTO;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.entity.Book;
import com.bookvault.bookvault.entity.Loan;
import com.bookvault.bookvault.entity.Member;
import com.bookvault.bookvault.event.LoanOverdueEvent;
import com.bookvault.bookvault.enums.LoanStatus;
import com.bookvault.bookvault.enums.MembershipStatus;
import com.bookvault.bookvault.exception.*;
import com.bookvault.bookvault.repository.BookRepository;
import com.bookvault.bookvault.repository.LoanRepository;
import com.bookvault.bookvault.repository.MemberRepository;
import com.bookvault.bookvault.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

        private static final long MAX_ACTIVE_LOANS_PER_MEMBER = 3;

        private final LoanRepository loanRepository;
        private final BookRepository bookRepository;
        private final MemberRepository memberRepository;
        private final ApplicationEventPublisher eventPublisher;

        @Override
        @Transactional
        public ResponseDTO borrowBook(LoanDTO dto) {

            // 1. Fetch Book
            Book book = bookRepository.findById(UUID.fromString(dto.getBookId()))
                    .orElseThrow(() -> new BookNotFoundException("BOOK_NOT_FOUND"));

            // 2. Fetch Member
            Member member = memberRepository.findById(UUID.fromString(dto.getMemberId()))
                    .orElseThrow(() -> new MemberNotFoundException("MEMBER_NOT_FOUND"));

            // 3. Validate book availability
            if (book.getAvailableCopies() <= 0) {
                throw new BookNotAvailableException("BOOK_NOT_AVAILABLE");
            }

            // 4. Validate member status
            if (member.getMembershipStatus() == MembershipStatus.SUSPENDED) {
                throw new MemberSuspendedException("MEMBER_SUSPENDED");
            }

            long activeLoanCount = loanRepository.countByMemberIdAndStatus(member.getId(), LoanStatus.ACTIVE);
            if (activeLoanCount >= MAX_ACTIVE_LOANS_PER_MEMBER) {
                throw new ActiveLoanLimitExceededException("ACTIVE_LOAN_LIMIT_EXCEEDED");
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

            bookRepository.save(book);
            loanRepository.save(loan);

            return ResponseDTO.success("Book borrowed successfully", loan.getId());
        }



    @Override
    @Transactional
    public ResponseDTO returnBook(String loanId) {

        // 1. Fetch Loan
        Loan loan = loanRepository.findById(UUID.fromString(loanId))
                .orElseThrow(() -> new LoanNotFoundException("LOAN_NOT_FOUND"));

        validateMemberCanAccessOwnLoan(loan);

        // 2. Check already returned
        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BookAlreadyReturnedException("BOOK_ALREADY_RETURNED");
        }

        // 3. Update Loan
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnedAt(LocalDateTime.now());

        // 4. Increment Book copies
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        // 5. Save
        loanRepository.save(loan);
        bookRepository.save(book);

        return ResponseDTO.success("Book returned successfully", loan.getId());
    }


    @Override
    public ResponseDTO getOverdueLoans(int page, int size, String sortBy, String direction) {
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Loan> overdueLoans = loanRepository
                .findByDueDateBeforeAndStatus(LocalDateTime.now(), LoanStatus.ACTIVE, pageable);

        overdueLoans.getContent().forEach(loan -> eventPublisher.publishEvent(
                new LoanOverdueEvent(loan.getId(), loan.getMember().getEmail())
        ));

        return ResponseDTO.success("Overdue loans fetched successfully", overdueLoans);
    }

    @Override
    public ResponseDTO getLoansByMember(String memberId) {

        UUID id = UUID.fromString(memberId);

        // optional validation (recommended)
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("MEMBER_NOT_FOUND"));

        validateMemberCanAccessOwnMemberData(member);

        List<Loan> loans = loanRepository.findByMemberId(id);

        return ResponseDTO.success("Member loan history fetched successfully", loans);
    }

    private void validateMemberCanAccessOwnLoan(Loan loan) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        boolean isMemberRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_MEMBER".equals(authority.getAuthority()));
        if (!isMemberRole) {
            return;
        }

        String username = authentication.getName();
        if (loan.getMember() == null || loan.getMember().getEmail() == null || !loan.getMember().getEmail().equalsIgnoreCase(username)) {
            throw new AccessDeniedException("MEMBER_CAN_ONLY_ACCESS_OWN_LOANS");
        }
    }

    private void validateMemberCanAccessOwnMemberData(Member member) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        boolean isMemberRole = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_MEMBER".equals(authority.getAuthority()));
        if (!isMemberRole) {
            return;
        }

        String username = authentication.getName();
        if (member.getEmail() == null || !member.getEmail().equalsIgnoreCase(username)) {
            throw new AccessDeniedException("MEMBER_CAN_ONLY_ACCESS_OWN_LOANS");
        }
    }
}
