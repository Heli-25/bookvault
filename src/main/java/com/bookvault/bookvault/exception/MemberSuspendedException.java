package com.bookvault.bookvault.exception;

public class MemberSuspendedException extends RuntimeException {
    private String message;
    public MemberSuspendedException(){

    }
    public MemberSuspendedException(String msg){
        super(msg);
        this.message=msg;
    }
}
