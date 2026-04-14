package com.bookvault.bookvault.serviceImpl;

import com.bookvault.bookvault.dto.LoanDTO;
import com.bookvault.bookvault.entity.Book;
import com.bookvault.bookvault.entity.Loan;
import com.bookvault.bookvault.entity.Member;
import com.bookvault.bookvault.enums.LoanStatus;
import com.bookvault.bookvault.enums.MembershipStatus;
import com.bookvault.bookvault.exception.ActiveLoanLimitExceededException;
import com.bookvault.bookvault.repository.BookRepository;
import com.bookvault.bookvault.repository.LoanRepository;
import com.bookvault.bookvault.repository.MemberRepository;
import com.bookvault.bookvault.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:loan-service-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class LoanServiceImplIntegrationTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @SpyBean
    private LoanRepository loanRepositorySpy;

    @BeforeEach
    void cleanDatabase() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void borrowBook_shouldRollbackIfLoanSaveFailsAfterBookUpdate() {
        Member member = createMember("rollback-member@example.com");
        Book book = createBook("rollback-isbn", 2);

        doThrow(new RuntimeException("SIMULATED_DB_FAILURE"))
                .when(loanRepositorySpy)
                .save(any(Loan.class));

        LoanDTO dto = new LoanDTO(null, book.getId().toString(), member.getId().toString());
        assertThrows(RuntimeException.class, () -> loanService.borrowBook(dto));

        Book persistedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertEquals(2, persistedBook.getAvailableCopies());
        assertEquals(0, loanRepository.count());
    }

    @Test
    void borrowBook_shouldRejectWhenMemberAlreadyHasThreeActiveLoans() {
        Member member = createMember("limit-member@example.com");
        Book requestedBook = createBook("requested-isbn", 5);

        createActiveLoan(member, createBook("active-isbn-1", 5));
        createActiveLoan(member, createBook("active-isbn-2", 5));
        createActiveLoan(member, createBook("active-isbn-3", 5));

        LoanDTO dto = new LoanDTO(null, requestedBook.getId().toString(), member.getId().toString());
        assertThrows(ActiveLoanLimitExceededException.class, () -> loanService.borrowBook(dto));
    }

    private Member createMember(String email) {
        Member member = new Member();
        member.setEmail(email);
        member.setName("Test Member");
        member.setMembershipStatus(MembershipStatus.ACTIVE);
        return memberRepository.save(member);
    }

    private Book createBook(String isbn, int copies) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle("Title " + isbn);
        book.setAuthor("Author");
        book.setGenre("Fiction");
        book.setTotalCopies(copies);
        book.setAvailableCopies(copies);
        return bookRepository.save(book);
    }

    private void createActiveLoan(Member member, Book book) {
        Loan loan = new Loan();
        loan.setMember(member);
        loan.setBook(book);
        loan.setBorrowedAt(LocalDateTime.now().minusDays(2));
        loan.setDueDate(LocalDateTime.now().plusDays(12));
        loan.setStatus(LoanStatus.ACTIVE);
        loanRepository.save(loan);
    }
}
