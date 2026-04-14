package com.bookvault.bookvault.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class LoanDTO {
    private String id;

    @NotBlank(message = "Book id is required")
    private String bookId;

    @NotBlank(message = "Member id is required")
    private String memberId;
}
