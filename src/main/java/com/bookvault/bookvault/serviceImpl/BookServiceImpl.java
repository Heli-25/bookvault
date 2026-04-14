package com.bookvault.bookvault.serviceImpl;

import com.bookvault.bookvault.config.CacheConfig;
import com.bookvault.bookvault.dto.BookDTO;
import com.bookvault.bookvault.dto.ResponseDTO;
import com.bookvault.bookvault.entity.Book;
import com.bookvault.bookvault.exception.BookNotFoundException;
import com.bookvault.bookvault.repository.BookRepository;
import com.bookvault.bookvault.service.BookService;
import com.bookvault.bookvault.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Cacheable(value = CacheConfig.BOOK_CATALOGUE_CACHE,
            key = "T(java.util.Objects).toString(#genre, '') + '|' + T(java.util.Objects).toString(#author, '') + '|' + T(java.util.Objects).toString(#available, '')")
    public ResponseDTO getBooks(String genre, String author, Boolean available) {
        Specification<Book> filters = BookSpecification.withFilters(genre, author, available);
        List<Book> books = bookRepository.findAll(filters);

        return ResponseDTO.success("Books fetched successfully", books);
    }

    @Override
    @CacheEvict(value = CacheConfig.BOOK_CATALOGUE_CACHE, allEntries = true)
    public ResponseDTO createBook(BookDTO dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getTotalCopies());

        Book savedBook = bookRepository.save(book);
        return ResponseDTO.success("Book created successfully", savedBook);
    }

    @Override
    @CacheEvict(value = CacheConfig.BOOK_CATALOGUE_CACHE, allEntries = true)
    public ResponseDTO updateBook(String bookId, BookDTO dto) {
        Book book = bookRepository.findById(UUID.fromString(bookId))
                .orElseThrow(() -> new BookNotFoundException("BOOK_NOT_FOUND"));

        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setTotalCopies(dto.getTotalCopies());
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        Book updatedBook = bookRepository.save(book);
        return ResponseDTO.success("Book updated successfully", updatedBook);
    }

    @Override
    @CacheEvict(value = CacheConfig.BOOK_CATALOGUE_CACHE, allEntries = true)
    public ResponseDTO deleteBook(String bookId) {
        Book book = bookRepository.findById(UUID.fromString(bookId))
                .orElseThrow(() -> new BookNotFoundException("BOOK_NOT_FOUND"));

        bookRepository.delete(book);
        return ResponseDTO.success("Book deleted successfully", bookId);
    }
}

