package com.bookvault.bookvault.exception;

public class BookAlreadyReturnedException extends RuntimeException{
    private String message;
    public BookAlreadyReturnedException(){

    }
    public BookAlreadyReturnedException(String msg){
        super(msg);
        this.message=msg;
    }
}
