package com.generic.typed.kakaoApi;


import com.generic.typed.dto.response.BookSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
    public ResponseEntity<BookSearchResponse> searchBooks(@RequestParam(name = "query") String query) {

        log.info("Search Query: {}", query);

        BookSearchResponse result = bookApi.searchBooks(query);

        log.info("Search Result: {}", result);
        log.info("Documents Count: {}",
                result.getDocuments() != null ? result.getDocuments().size() : "null");

        if (result.getDocuments() == null || result.getDocuments().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }

}