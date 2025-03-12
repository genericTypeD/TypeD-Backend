package com.generic.typed.dto.response;

import com.generic.typed.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewCreateResponse {
    private final Long id;
    private final String content;
    private final boolean isPublic;
    private final LocalDateTime createdAt;
    private final String bookTitle;
    private final String bookIsbn;
    private final String thumbnail;

    public static ReviewCreateResponse from(Review review) {
        return ReviewCreateResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .isPublic(review.isPublic())
                .createdAt(review.getCreatedAt())
                .bookTitle(review.getBookTitle())
                .bookIsbn(review.getIsbn())
                .thumbnail(review.getBookThumbnail())
                .build();
    }
}