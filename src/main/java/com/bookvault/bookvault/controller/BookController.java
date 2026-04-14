package com.bookvault.bookvault.controller;

import com.bookvault.bookvault.dto.BookDTO;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseDTO createBook(@Valid @RequestBody BookDTO dto) {
        return bookService.createBook(dto);
    }

    @PutMapping("/{id}")
    public ResponseDTO updateBook(@PathVariable String id, @Valid @RequestBody BookDTO dto) {
        return bookService.updateBook(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseDTO deleteBook(@PathVariable String id) {
        return bookService.deleteBook(id);
    }
}

