package com.bookvault.bookvault.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseDTO {
    private String status;
    private String message;
    private Object data;
}
