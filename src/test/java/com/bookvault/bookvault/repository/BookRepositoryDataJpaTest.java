package com.bookvault.bookvault.repository;

import com.bookvault.bookvault.entity.Book;
import com.bookvault.bookvault.specification.BookSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookRepositoryDataJpaTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findAll_withSpecification_shouldFilterByGenreAuthorAndAvailability() {
        bookRepository.save(buildBook("isbn-1", "Domain-Driven Design", "Eric Evans", "Tech", 3, 2));
        bookRepository.save(buildBook("isbn-2", "Refactoring", "Martin Fowler", "Tech", 4, 0));
        bookRepository.save(buildBook("isbn-3", "Sapiens", "Yuval Noah Harari", "History", 5, 5));

        List<Book> filtered = bookRepository.findAll(
                BookSpecification.withFilters("tech", "martin", false)
        );

        assertEquals(1, filtered.size());
        assertEquals("isbn-2", filtered.get(0).getIsbn());
    }

    private Book buildBook(String isbn, String title, String author, String genre, int totalCopies, int availableCopies) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(availableCopies);
        return book;
    }
}
