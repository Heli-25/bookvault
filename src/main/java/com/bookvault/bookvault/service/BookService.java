package com.bookvault.bookvault.service;

import com.bookvault.bookvault.dto.ResponseDTO;

public interface BookService {
    ResponseDTO getBooks(String genre, String author, Boolean available);
}
