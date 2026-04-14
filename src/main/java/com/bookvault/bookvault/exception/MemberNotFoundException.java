package com.bookvault.bookvault.exception;

public class MemberNotFoundException extends RuntimeException{
    private String message;
    public MemberNotFoundException(){

    }
    public MemberNotFoundException(String msg){
        super(msg);
        this.message=msg;
    }
}
