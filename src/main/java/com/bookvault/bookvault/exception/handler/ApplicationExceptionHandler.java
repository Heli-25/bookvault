package com.bookvault.bookvault.exception.handler;

import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage() == null ? "Validation failed" : error.getDefaultMessage())
                .orElse("Validation failed");

        return new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), message, null);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO handleUserAlreadyExists(BookNotAvailableException exception){
        return new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), exception.getMessage(), null);
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO handleInvalidUserRoleException(BookNotFoundException exception) {
        return new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), exception.getMessage(), null);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO handleUserAlreadyExists(MemberNotFoundException exception){
        return new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), exception.getMessage(), null);
    }

    @ExceptionHandler(MemberSuspendedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO handleInvalidUserRoleException(MemberSuspendedException exception) {
        return new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), exception.getMessage(), null);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO handleInvalidUserRoleException(LoanNotFoundException exception) {
        return new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), exception.getMessage(), null);
    }

    @ExceptionHandler(BookAlreadyReturnedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO handleInvalidUserRoleException(BookAlreadyReturnedException exception) {
        return new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), exception.getMessage(), null);
    }

    @ExceptionHandler(ActiveLoanLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseDTO handleActiveLoanLimitExceededException(ActiveLoanLimitExceededException exception) {
        return new ResponseDTO(String.valueOf(HttpStatus.BAD_REQUEST.value()), exception.getMessage(), null);
    }
}
