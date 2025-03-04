package com.generic.typed.service;

import com.generic.typed.kakaoApi.KakaoBookApi;
import com.generic.typed.domain.Member;
import com.generic.typed.domain.Review;
import com.generic.typed.dto.response.MyReviewResponse;
import com.generic.typed.repository.ReviewRepository;
import com.generic.typed.dto.request.ReviewCreateRequest;
import com.generic.typed.dto.response.BookSearchResponse;
import com.generic.typed.dto.response.MyReviewListResponse;
import com.generic.typed.dto.response.ReviewCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final KakaoBookApi bookApi;

    @Transactional
    public ReviewCreateResponse createReview(String userIdentifier, ReviewCreateRequest request) {
        System.out.println("Service - UserIdentifier at start: " + userIdentifier);

        // 요청 검증
        request.validate();

        // 사용자 정보 조회 (로그인한 경우만)
        Member member = null;
        if (userIdentifier != null && userIdentifier.contains("@")) {
            try {
                member = memberService.findByEmail(userIdentifier);
            } catch (Exception e) {
                System.out.println("사용자를 찾을 수 없습니다: " + userIdentifier);
            }
        }

        // 책 정보 수집
        String bookTitle = request.getBookTitle();
        String bookAuthor = ""; // 클라이언트에서 제공하지 않으면 비워둠
        String bookThumbnail = request.getThumbnail();
        String isbn = request.getBookIsbn();

        // 추가 정보가 필요하면 ISBN으로 책 정보 조회 가능
        if (isbn != null && !isbn.isEmpty() && (bookAuthor == null || bookAuthor.isEmpty())) {
            try {
                BookSearchResponse.BookDocument book = bookApi.getBookByIsbn(isbn);
                if (book != null) {
                    bookAuthor = String.join(", ", book.getAuthors());
                    // 썸네일이 없는 경우에만 API에서 가져옴
                    if (bookThumbnail == null || bookThumbnail.isEmpty()) {
                        bookThumbnail = book.getThumbnail();
                    }
                }
            } catch (Exception e) {
                System.out.println("책 정보 조회 실패: " + e.getMessage());
            }
        }

        // 새 서평 엔티티 생성
        Review review = new Review(
                request.getContent(),
                request.isPublic(),
                LocalDateTime.now(),
                member,
                member == null ? userIdentifier : null,
                bookTitle,
                bookAuthor,
                bookThumbnail,
                isbn
        );

        // 저장
        Review savedReview = reviewRepository.save(review);

        // 응답 생성
        return ReviewCreateResponse.from(savedReview);
    }

    @Transactional(readOnly = true)
    public MyReviewListResponse getPublicReviews() {
        List<Review> reviews = reviewRepository.findByIsPublicTrue();
        return MyReviewListResponse.from(reviews);
    }

    @Transactional(readOnly = true)
    public MyReviewListResponse getMyReviews(String userIdentifier) {
        List<Review> reviews;

        if (userIdentifier.contains("@")) {
            try {
                Member member = memberService.findByEmail(userIdentifier);
                reviews = reviewRepository.findByMember(member);
            } catch (Exception e) {
                // 이메일 형식이지만 회원이 아닌 경우 빈 목록 반환
                reviews = List.of();
            }
        } else {
            // 디바이스 ID로 검색
            reviews = reviewRepository.findByDeviceId(userIdentifier);
        }

        return MyReviewListResponse.from(reviews);
    }

    @Transactional
    public MyReviewResponse updateReview(String userIdentifier, Long reviewId, ReviewCreateRequest request) {
        // 요청 검증
        request.validate();

        Review review;

        if (userIdentifier.contains("@")) {
            try {
                Member member = memberService.findByEmail(userIdentifier);
                review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서평입니다."));

                if (!review.getMember().equals(member)) {
                    throw new IllegalArgumentException("해당 서평을 수정할 권한이 없습니다.");
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalArgumentException("서평 수정 중 오류가 발생했습니다.");
            }
        } else {
            review = reviewRepository.findByIdAndDeviceId(reviewId, userIdentifier)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서평입니다."));
        }

        review.update(request.getContent(), request.isPublic());

        return MyReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(String userIdentifier, Long reviewId) {
        Review review;

        if (userIdentifier.contains("@")) {
            try {
                Member member = memberService.findByEmail(userIdentifier);
                review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서평입니다."));

                if (!review.getMember().equals(member)) {
                    throw new IllegalArgumentException("해당 서평을 삭제할 권한이 없습니다.");
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalArgumentException("서평 삭제 중 오류가 발생했습니다.");
            }
        } else {
            review = reviewRepository.findByIdAndDeviceId(reviewId, userIdentifier)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서평입니다."));
        }

        reviewRepository.delete(review);
    }
}