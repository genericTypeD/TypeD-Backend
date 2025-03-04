package com.generic.typed.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BookSearchResponse {
    private List<BookDocument> documents;
    private BookMeta meta;

    @Data
    public static class BookDocument {
        private List<String> authors;
        private String contents = "";
        private String datetime = "";
        private String isbn = "";
        private int price;
        private String publisher;

        @JsonProperty("sale_price")
        private int salePrice;

        private String status;
        private String thumbnail;
        private String title;
        private List<String> translators;
        private String url;
    }

    @Data
    public static class BookMeta {
        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("total_count")
        private int totalCount;
    }
}