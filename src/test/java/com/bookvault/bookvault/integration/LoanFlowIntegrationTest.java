package com.bookvault.bookvault.integration;

import com.bookvault.bookvault.dto.LoanDTO;
import com.bookvault.bookvault.entity.Book;
import com.bookvault.bookvault.entity.Loan;
import com.bookvault.bookvault.entity.Member;
import com.bookvault.bookvault.enums.LoanStatus;
import com.bookvault.bookvault.enums.MembershipStatus;
import com.bookvault.bookvault.repository.BookRepository;
import com.bookvault.bookvault.repository.LoanRepository;
import com.bookvault.bookvault.repository.MemberRepository;
import com.bookvault.bookvault.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:loan-flow-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc(addFilters = false)
class LoanFlowIntegrationTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void cleanDatabase() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void borrowThenReturn_shouldCompleteFullFlow() {
        Member member = createMember("flow-member@example.com");
        Book book = createBook("flow-isbn", 2);

        LoanDTO dto = new LoanDTO(null, book.getId().toString(), member.getId().toString());
        UUID loanId = (UUID) loanService.borrowBook(dto).getData();

        Loan borrowedLoan = loanRepository.findById(loanId).orElseThrow();
        Book bookAfterBorrow = bookRepository.findById(book.getId()).orElseThrow();
        assertEquals(LoanStatus.ACTIVE, borrowedLoan.getStatus());
        assertEquals(1, bookAfterBorrow.getAvailableCopies());

        loanService.returnBook(loanId.toString());

        Loan returnedLoan = loanRepository.findById(loanId).orElseThrow();
        Book bookAfterReturn = bookRepository.findById(book.getId()).orElseThrow();
        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus());
        assertNotNull(returnedLoan.getReturnedAt());
        assertEquals(2, bookAfterReturn.getAvailableCopies());
    }

    @Test
    void borrowBook_whenAvailableCopiesZero_shouldReturnBadRequestErrorResponse() throws Exception {
        Member member = createMember("negative-member@example.com");
        Book unavailableBook = createBook("negative-isbn", 0);

        String payload = """
                {
                  "bookId": "%s",
                  "memberId": "%s"
                }
                """.formatted(unavailableBook.getId(), member.getId());

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.code").value("BOOK_NOT_AVAILABLE"))
                .andExpect(jsonPath("$.error.message").value("BOOK_NOT_AVAILABLE"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    private Member createMember(String email) {
        Member member = new Member();
        member.setEmail(email);
        member.setName("Integration Member");
        member.setMembershipStatus(MembershipStatus.ACTIVE);
        return memberRepository.save(member);
    }

    private Book createBook(String isbn, int availableCopies) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle("Book " + isbn);
        book.setAuthor("Author");
        book.setGenre("Tech");
        book.setTotalCopies(Math.max(availableCopies, 1));
        book.setAvailableCopies(availableCopies);
        return bookRepository.save(book);
    }
}
