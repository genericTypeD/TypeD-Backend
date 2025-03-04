package com.generic.typed.controller;


import com.generic.kakaoApi.KakaoBookApi;
import com.generic.typed.response.BookSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final KakaoBookApi bookApi;

    /**
     * 책 검색 API
     * 클라이언트에서는 먼저 이 API를 호출하여 책을 검색하고,
     * 선택한 책의 ISBN으로 서평을 작성합니다.
     */
    @GetMapping("/search")
    public ResponseEntity<BookSearchResponse> searchBooks(@RequestParam String query) {
        BookSearchResponse result = bookApi.searchBooks(query);
        return ResponseEntity.ok(result);
    }
}