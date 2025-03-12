package com.generic.typed.repository;

import com.generic.typed.dto.response.BookSearchResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSearchRepository {
    BookSearchResponse searchBooks(String query);
    BookSearchResponse searchBooksByIsbn(String isbn);
}
