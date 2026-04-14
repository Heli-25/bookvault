package com.bookvault.bookvault.exception.handler;

import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage() == null ? "Validation failed" : error.getDefaultMessage())
                .orElse("Validation failed");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", message);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ResponseDTO> handleBookNotAvailable(BookNotAvailableException exception){
        return buildErrorResponse(HttpStatus.CONFLICT, "BOOK_NOT_AVAILABLE", exception.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleBookNotFound(BookNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleMemberNotFound(MemberNotFoundException exception){
        return buildErrorResponse(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(MemberSuspendedException.class)
    public ResponseEntity<ResponseDTO> handleMemberSuspended(MemberSuspendedException exception) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "MEMBER_SUSPENDED", exception.getMessage());
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleLoanNotFound(LoanNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "LOAN_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(BookAlreadyReturnedException.class)
    public ResponseEntity<ResponseDTO> handleBookAlreadyReturned(BookAlreadyReturnedException exception) {
        return buildErrorResponse(HttpStatus.CONFLICT, "BOOK_ALREADY_RETURNED", exception.getMessage());
    }

    @ExceptionHandler(ActiveLoanLimitExceededException.class)
    public ResponseEntity<ResponseDTO> handleActiveLoanLimitExceededException(ActiveLoanLimitExceededException exception) {
        return buildErrorResponse(HttpStatus.CONFLICT, "ACTIVE_LOAN_LIMIT_EXCEEDED", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGenericException(Exception exception) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", exception.getMessage());
    }

    private ResponseEntity<ResponseDTO> buildErrorResponse(HttpStatus status, String code, String message) {
        Map<String, Object> errorPayload = Map.of(
                "code", code,
                "message", message
        );
        return ResponseEntity.status(status).body(new ResponseDTO(null, errorPayload));
    }
}
