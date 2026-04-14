package com.bookvault.bookvault.exception;

public class ActiveLoanLimitExceededException extends RuntimeException {

    public ActiveLoanLimitExceededException(String message) {
        super(message);
    }
}
