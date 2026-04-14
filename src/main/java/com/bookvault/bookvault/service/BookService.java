package com.bookvault.bookvault.service;

import com.bookvault.bookvault.dto.BookDTO;
import com.bookvault.bookvault.dto.ResponseDTO;

public interface BookService {
    ResponseDTO getBooks(String genre, String author, Boolean available);

    ResponseDTO createBook(BookDTO dto);

    ResponseDTO updateBook(String bookId, BookDTO dto);

    ResponseDTO deleteBook(String bookId);
}
