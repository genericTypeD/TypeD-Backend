package com.generic.typed.dto.response;

import com.generic.typed.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MyReviewListResponse {
    private final List<MyReviewResponse> reviews;

    @Builder
    public MyReviewListResponse(List<MyReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public static MyReviewListResponse from(List<Review> reviews) {
        List<MyReviewResponse> reviewResponses = reviews.stream()
                .map(MyReviewResponse::from)
                .collect(Collectors.toList());

        return MyReviewListResponse.builder()
                .reviews(reviewResponses)
                .build();
    }
}