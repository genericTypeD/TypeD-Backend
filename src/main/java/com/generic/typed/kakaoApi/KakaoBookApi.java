package com.generic.typed.kakaoApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.typed.dto.response.BookSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class KakaoBookApi {

    private final RestTemplate restTemplate;
    private final String kakaoApiKey;
    private static final String KAKAO_BOOK_API_URL = "https://dapi.kakao.com/v3/search/book";

    public KakaoBookApi(@Value("${kakao.api.key}") String kakaoApiKey) {
        this.restTemplate = new RestTemplate();
        this.kakaoApiKey = kakaoApiKey;
    }

    public BookSearchResponse searchBooks(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            URL url = new URL(KAKAO_BOOK_API_URL + "?query=" + encodedQuery + "&size=10");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoApiKey);
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            log.info("Response Code: {}", responseCode);

            BufferedReader in;
            if (responseCode >= 200 && responseCode <= 300) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            log.info("Full Response: {}", response.toString());

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.toString(), BookSearchResponse.class);

        } catch (Exception e) {
            log.error("책 검색 API 호출 중 오류", e);
            return new BookSearchResponse();
        }
    }

    public BookSearchResponse.BookDocument getBookByIsbn(String isbn) {
        BookSearchResponse result = searchBooks("isbn:" + isbn);
        if (result.getDocuments() != null && !result.getDocuments().isEmpty()) {
            return result.getDocuments().get(0);
        }
        return null;
    }
}
