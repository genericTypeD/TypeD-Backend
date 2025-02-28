성package com.generic.kakaoApi;

import com.generic.typed.response.BookSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoBookApi {

    private final RestTemplate restTemplate;
    private final String kakaoApiKey;
    private static final String KAKAO_BOOK_API_URL = "https://dapi.kakao.com/v3/search/book";

    public KakaoBookApi(RestTemplate restTemplate, @Value("${kakao.api.key}") String kakaoApiKey) {
        this.restTemplate = restTemplate;
        this.kakaoApiKey = kakaoApiKey;
    }

    public BookSearchResponse searchBooks(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_BOOK_API_URL)
                .queryParam("query", query);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<BookSearchResponse> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                BookSearchResponse.class
        );

        return response.getBody();
    }

    public BookSearchResponse.BookDocument getBookByIsbn(String isbn) {
        BookSearchResponse result = searchBooks("isbn:" + isbn);
        if (result.getDocuments() != null && !result.getDocuments().isEmpty()) {
            return result.getDocuments().get(0);
        }
        return null;
    }
}
