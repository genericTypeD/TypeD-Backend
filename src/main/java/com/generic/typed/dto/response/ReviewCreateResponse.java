package com.generic.typed.response;

import com.generic.typed.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewCreateResponse {
    private final Long reviewId;
    private final String content;
    private final boolean isPublic;
    private final LocalDateTime createdAt;
    private final String bookTitle;
    private final String bookAuthor;
    private final String bookThumbnail;

    public static ReviewCreateResponse from(Review review) {
        return ReviewCreateResponse.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .isPublic(review.isPublic())
                .createdAt(review.getCreatedAt())
                .bookTitle(review.getBookTitle())
                .bookAuthor(review.getBookAuthor())
                .bookThumbnail(review.getBookThumbnail())
                .build();
    }
}