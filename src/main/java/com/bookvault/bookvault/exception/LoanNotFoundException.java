package com.bookvault.bookvault.exception;

public class LoanNotFoundException extends RuntimeException {
    private String message;
    public LoanNotFoundException(){

    }
    public LoanNotFoundException(String msg){
        super(msg);
        this.message=msg;
    }
}
