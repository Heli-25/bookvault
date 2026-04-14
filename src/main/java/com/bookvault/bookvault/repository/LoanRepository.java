package com.bookvault.bookvault.repository;

import com.bookvault.bookvault.entity.Loan;
import com.bookvault.bookvault.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {

    List<Loan> findByDueDateBeforeAndStatus(LocalDateTime date, LoanStatus status);

    Page<Loan> findByDueDateBeforeAndStatus(LocalDateTime date, LoanStatus status, Pageable pageable);

    List<Loan> findByMemberId(UUID memberId);

}
