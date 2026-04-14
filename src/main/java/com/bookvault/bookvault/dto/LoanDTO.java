package com.bookvault.bookvault.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class LoanDTO {
    private String id;
    private String bookId;
    private String memberId;
}
