package com.bookvault.bookvault.serviceImpl;

import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.entity.Book;
import com.bookvault.bookvault.repository.BookRepository;
import com.bookvault.bookvault.service.BookService;
import com.bookvault.bookvault.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public ResponseDTO getBooks(String genre, String author, Boolean available) {
        Specification<Book> filters = BookSpecification.withFilters(genre, author, available);
        List<Book> books = bookRepository.findAll(filters);

        return new ResponseDTO("SUCCESS", "Books fetched successfully", books);
    }
}

