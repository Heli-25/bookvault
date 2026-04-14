package com.bookvault.bookvault.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseDTO {
    private Object data;
    private Object error;
    private LocalDateTime timestamp;

    public ResponseDTO(Object data) {
        this.data = data;
        this.error = null;
        this.timestamp = LocalDateTime.now();
    }

    public ResponseDTO(Object data, Object error) {
        this.data = data;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }

    public static ResponseDTO success(String message, Object result) {
        return new ResponseDTO(Map.of("message", message, "result", result), null);
    }

    public static ResponseDTO success(Object result) {
        return new ResponseDTO(result, null);
    }

    public static ResponseDTO failure(String code, String message) {
        return new ResponseDTO(null, Map.of("code", code, "message", message));
    }
}
