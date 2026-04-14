package com.bookvault.bookvault.exception;

public class BookNotFoundException extends RuntimeException{
    private String message;
    public BookNotFoundException(){

    }
    public BookNotFoundException(String msg){
        super(msg);
        this.message=msg;
    }
}
