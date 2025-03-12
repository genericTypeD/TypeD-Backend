package com.generic.typed.dto.response;

import com.generic.typed.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MyReviewListResponse {
    private final List<MyReviewResponse> reviews;

    @Builder
    public MyReviewListResponse(List<MyReviewResponse> reviews) {
        // reviews가 null인 경우 빈 리스트로 초기화
        this.reviews = reviews != null ? reviews : Collections.emptyList();
    }

    public static MyReviewListResponse from(List<Review> reviews) {
        if (reviews == null) {
            return new MyReviewListResponse(Collections.emptyList());
        }

        List<MyReviewResponse> reviewResponses = reviews.stream()
                .map(MyReviewResponse::from)
                .collect(Collectors.toList());

        return MyReviewListResponse.builder()
                .reviews(reviewResponses)
                .build();
    }
}