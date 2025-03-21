package com.generic.typed.service;

import com.generic.typed.domain.Member;
import com.generic.typed.domain.Review;
import com.generic.typed.dto.request.ReviewCreateRequest;
import com.generic.typed.dto.response.BookSearchResponse;
import com.generic.typed.dto.response.MyReviewListResponse;
import com.generic.typed.dto.response.MyReviewResponse;
import com.generic.typed.dto.response.ReviewCreateResponse;
import com.generic.typed.kakaoApi.KakaoBookApi;
import com.generic.typed.repository.ReviewRepository;
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
    public ReviewCreateResponse createReview(String email, ReviewCreateRequest request) {
        // 요청 검증
        request.validate();

        // 사용자 정보 조회 (로그인 필수)
        Member member = memberService.findByEmail(email);

        // 책 정보 조회
        String bookTitle = "";
        String bookAuthor = "";
        String bookThumbnail = "";
        String isbn = request.getBookIsbn();

        String bookQuery = request.getBookTitle();
        if (bookQuery != null && !bookQuery.isEmpty()) {
            BookSearchResponse searchResponse = bookApi.searchBooks(bookQuery);
            if (searchResponse != null && searchResponse.getDocuments() != null && !searchResponse.getDocuments().isEmpty()) {
                // 첫 번째 검색 결과 사용 (또는 사용자가 선택한 결과 사용)
                BookSearchResponse.BookDocument book = searchResponse.getDocuments().get(0);
                bookTitle = book.getTitle();
                bookAuthor = String.join(", ", book.getAuthors());
                bookThumbnail = book.getThumbnail();
                isbn = book.getIsbn();
            }
        }

        // 새 서평 엔티티 생성 (deviceId 없음)
        Review review = new Review(
                request.getContent(),
                request.isPublic(),
                LocalDateTime.now(),
                member,
                null, // deviceId 제거 (null로 전달)
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
    public MyReviewListResponse getMyReviews(String email) {
        // 로그인한 사용자의 리뷰만 조회
        Member member = memberService.findByEmail(email);
        List<Review> reviews = reviewRepository.findByMember(member);
        return MyReviewListResponse.from(reviews);
    }

    @Transactional
    public MyReviewResponse updateReview(String email, Long reviewId, ReviewCreateRequest request) {
        // 요청 검증
        request.validate();

        // 로그인 사용자 확인
        Member member = memberService.findByEmail(email);

        // 리뷰 조회 및 권한 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서평입니다."));

        if (!review.getMember().equals(member)) {
            throw new IllegalArgumentException("해당 서평을 수정할 권한이 없습니다.");
        }

        // 서평 업데이트
        review.update(request.getContent(), request.isPublic());

        return MyReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(String email, Long reviewId) {
        // 로그인 사용자 확인
        Member member = memberService.findByEmail(email);

        // 리뷰 조회 및 권한 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서평입니다."));

        if (!review.getMember().equals(member)) {
            throw new IllegalArgumentException("해당 서평을 삭제할 권한이 없습니다.");
        }

        // 서평 삭제
        reviewRepository.delete(review);
    }
}