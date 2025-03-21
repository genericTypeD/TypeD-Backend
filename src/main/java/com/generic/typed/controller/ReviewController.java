package com.generic.typed.controller;

import com.generic.typed.dto.request.ReviewCreateRequest;
import com.generic.typed.dto.response.MyReviewListResponse;
import com.generic.typed.dto.response.MyReviewResponse;
import com.generic.typed.dto.response.ReviewCreateResponse;
import com.generic.typed.security.jwt.util.IfLogin;
import com.generic.typed.security.jwt.util.LoginMemberDto;
import com.generic.typed.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 서평 등록 - 로그인 필요
     */
    @PostMapping
    public ResponseEntity<ReviewCreateResponse> createReview(
            @IfLogin LoginMemberDto loginMemberDto,
            @RequestBody ReviewCreateRequest request) {

        ReviewCreateResponse response = reviewService.createReview(loginMemberDto.getEmail(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 내 서평 목록 조회 - 로그인 필요
     */
    @GetMapping("/my")
    public ResponseEntity<MyReviewListResponse> getMyReviews(
            @IfLogin LoginMemberDto loginMemberDto) {

        MyReviewListResponse response = reviewService.getMyReviews(loginMemberDto.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * 서평 수정 - 로그인 필요
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<MyReviewResponse> updateReview(
            @IfLogin LoginMemberDto loginMemberDto,
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewCreateRequest request) {

        MyReviewResponse response = reviewService.updateReview(loginMemberDto.getEmail(), reviewId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 서평 삭제 - 로그인 필요
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @IfLogin LoginMemberDto loginMemberDto,
            @PathVariable("reviewId") Long reviewId) {

        reviewService.deleteReview(loginMemberDto.getEmail(), reviewId);
        return ResponseEntity.ok().build();
    }
}