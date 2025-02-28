package com.generic.typed.response;

import lombok.Data;

import java.util.List;

@Data
public class BookSearchResponse {
    private List<BookDocument> documents;
    private BookMeta meta;

    @Data
    public static class BookDocument {
        private List<String> authors;
        private String contents;
        private String datetime;
        private String isbn;
        private int price;
        private String publisher;
        private int salePrice;
        private String status;
        private String thumbnail;
        private String title;
        private List<String> translators;
        private String url;
    }

    @Data
    public static class BookMeta {
        private boolean isEnd;
        private int pageableCount;
        private int totalCount;
    }
}