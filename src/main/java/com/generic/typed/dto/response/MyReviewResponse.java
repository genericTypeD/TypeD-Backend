package com.generic.typed.dto.response;

import com.generic.typed.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyReviewResponse {
    private final Long id;
    private final String content;
    private final boolean isPublic;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String bookTitle;
    private final String bookAuthor;
    private final String bookThumbnail;
    private final String authorNickname;

    @Builder
    public MyReviewResponse(Long id, String content, boolean isPublic, LocalDateTime createdAt,
                          LocalDateTime updatedAt, String bookTitle, String bookAuthor,
                          String bookThumbnail, String authorNickname) {
        this.id = id;
        this.content = content;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookThumbnail = bookThumbnail;
        this.authorNickname = authorNickname;
    }

    public static MyReviewResponse from(Review review) {
        return MyReviewResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .isPublic(review.isPublic())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .bookTitle(review.getBookTitle())
                .bookAuthor(review.getBookAuthor())
                .bookThumbnail(review.getBookThumbnail())
                .authorNickname(review.getMember() != null ? review.getMember().getNickname() : "익명")
                .build();
    }
}