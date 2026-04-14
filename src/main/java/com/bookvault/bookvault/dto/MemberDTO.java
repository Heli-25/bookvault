package com.bookvault.bookvault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDTO {
    private String id;

    @Email
    private String email;

    @NotBlank
    private String name;
}
