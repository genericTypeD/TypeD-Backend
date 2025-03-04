package com.generic.typed.controller;

import com.generic.typed.dto.response.MyReviewResponse;
import com.generic.typed.dto.request.ReviewCreateRequest;
import com.generic.typed.dto.response.MyReviewListResponse;
import com.generic.typed.dto.response.ReviewCreateResponse;
import com.generic.typed.security.jwt.util.IfLogin;
import com.generic.typed.security.jwt.util.LoginMemberDto;
import com.generic.typed.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 서평 등록 - 로그인 불필요
     * 로그인하지 않은 사용자는 deviceId로 구분
     */
    @PostMapping
    public ResponseEntity<ReviewCreateResponse> createReview(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @IfLogin(required = false) LoginMemberDto loginMemberDto,
            @RequestBody ReviewCreateRequest request) {

        System.out.println("Controller - X-Device-Id Header: " + deviceId);
        System.out.println("Controller - LoginMemberDto: " + loginMemberDto);

        // 로그인한 경우 email 사용, 비로그인 시 deviceId 사용
        String userIdentifier = loginMemberDto != null
                ? loginMemberDto.getEmail()
                : (deviceId != null ? deviceId : UUID.randomUUID().toString());

        System.out.println("Controller - UserIdentifier before service call: " + userIdentifier);

        ReviewCreateResponse response = reviewService.createReview(userIdentifier, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 내 서평 목록 조회 - 로그인 불필요
     * deviceId로 서평 소유자 구분
     */
    @GetMapping("/my")
    public ResponseEntity<MyReviewListResponse> getMyReviews(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @IfLogin(required = false) LoginMemberDto loginMemberDto) {

        String userIdentifier = loginMemberDto != null
                ? loginMemberDto.getEmail()
                : (deviceId != null ? deviceId : "anonymous");

        MyReviewListResponse response = reviewService.getMyReviews(userIdentifier);
        return ResponseEntity.ok(response);
    }


    /**
     * 서평 수정 - 로그인 불필요, deviceId로 소유자 확인
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<MyReviewResponse> updateReview(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @IfLogin(required = false) LoginMemberDto loginMemberDto,
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewCreateRequest request) {

        String userIdentifier = loginMemberDto != null
                ? loginMemberDto.getEmail()
                : (deviceId != null ? deviceId : "anonymous");

        MyReviewResponse response = reviewService.updateReview(userIdentifier, reviewId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 서평 삭제 - 로그인 불필요, deviceId로 소유자 확인
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @IfLogin(required = false) LoginMemberDto loginMemberDto,
            @PathVariable("reviewId") Long reviewId) {

        String userIdentifier = loginMemberDto != null
                ? loginMemberDto.getEmail()
                : (deviceId != null ? deviceId : "anonymous");

        reviewService.deleteReview(userIdentifier, reviewId);
        return ResponseEntity.ok().build();
    }
}