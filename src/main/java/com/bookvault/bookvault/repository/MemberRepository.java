package com.bookvault.bookvault.repository;

import com.bookvault.bookvault.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    List<Member> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
}
