package com.bookvault.bookvault.specification;

import com.bookvault.bookvault.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecification {

    private BookSpecification() {
    }

    public static Specification<Book> withFilters(String genre, String author, Boolean available) {
        return Specification.where(hasGenre(genre))
                .and(hasAuthor(author))
                .and(hasAvailability(available));
    }

    private static Specification<Book> hasGenre(String genre) {
        return (root, query, cb) -> {
            if (genre == null || genre.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("genre")), genre.trim().toLowerCase());
        };
    }

    private static Specification<Book> hasAuthor(String author) {
        return (root, query, cb) -> {
            if (author == null || author.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("author")), "%" + author.trim().toLowerCase() + "%");
        };
    }

    private static Specification<Book> hasAvailability(Boolean available) {
        return (root, query, cb) -> {
            if (available == null) {
                return cb.conjunction();
            }
            return available
                    ? cb.greaterThan(root.get("availableCopies"), 0)
                    : cb.equal(root.get("availableCopies"), 0);
        };
    }
}




  
