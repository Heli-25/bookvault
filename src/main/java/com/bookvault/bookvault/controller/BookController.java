package com.bookvault.bookvault.controller;

import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseDTO getBooks(@RequestParam(required = false) String genre,
                                @RequestParam(required = false) String author,
                                @RequestParam(required = false) Boolean available) {
        return bookService.getBooks(genre, author, available);
    }
}

