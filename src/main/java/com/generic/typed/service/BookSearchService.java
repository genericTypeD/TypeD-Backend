package com.generic.typed.service;

import com.generic.typed.kakaoApi.KakaoBookApi;
import com.generic.typed.dto.response.BookSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookSearchService {
    private final KakaoBookApi kakaoBookApi;

    public BookSearchResponse searchBooks(String query) {
        return kakaoBookApi.searchBooks(query);
    }

    public BookSearchResponse searchBooksByIsbn(String isbn) {
        return kakaoBookApi.searchBooks("isbn:" + isbn);
    }
}