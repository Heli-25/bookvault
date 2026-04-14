package com.bookvault.bookvault.entity;

import com.bookvault.bookvault.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue
    private UUID id;

    // MANY LOANS → ONE BOOK
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // MANY LOANS → ONE MEMBER
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private LocalDateTime borrowedAt;

    private LocalDateTime dueDate;

    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @PrePersist
    public void setBorrowDetails() {
        this.borrowedAt = LocalDateTime.now();
        this.dueDate = borrowedAt.plusDays(14);
        this.status = LoanStatus.ACTIVE;
    }
}
