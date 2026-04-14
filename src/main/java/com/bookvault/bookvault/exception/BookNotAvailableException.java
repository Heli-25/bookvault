package com.bookvault.bookvault.exception;

public class BookNotAvailableException extends RuntimeException {

  private String message;
  public BookNotAvailableException(){

  }
  public BookNotAvailableException(String msg){
    super(msg);
    this.message=msg;
  }
}
