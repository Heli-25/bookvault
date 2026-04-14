package com.bookvault.bookvault.entity;

import com.bookvault.bookvault.enums.MembershipStatus;
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
public class Member {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private MembershipStatus membershipStatus;

    private LocalDateTime joinedAt;

    @PrePersist
    public void setJoinedAt() {
        this.joinedAt = LocalDateTime.now();
    }
}
