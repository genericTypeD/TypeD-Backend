package com.generic.typed.service;

import com.generic.typed.domain.Bookmark;
import com.generic.typed.domain.Member;
import com.generic.typed.domain.Review;
import com.generic.typed.domain.Sentence;
import com.generic.typed.dto.response.FeedItemResponse;
import com.generic.typed.dto.response.FeedListResponse;
import com.generic.typed.repository.BookmarkRepository;
import com.generic.typed.repository.MemberRepository;
import com.generic.typed.repository.ReviewRepository;
import com.generic.typed.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {
    private final SentenceRepository sentenceRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;

    /**
     * 전체 피드 목록 조회 (페이징 처리)
     */
    public FeedListResponse getFeedList(String email, int page, int size) {
        Member currentMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 북마크한 컨텐츠 ID 목록 조회 (현재 로그인한 사용자 기준)
        List<Bookmark> bookmarks = bookmarkRepository.findByMember(currentMember);
        Set<String> bookmarkedIds = bookmarks.stream()
                .map(b -> b.getContentType() + ":" + b.getContentId())
                .collect(Collectors.toSet());

        // 공개된 문장 조회
        List<Sentence> sentences = sentenceRepository.findByIsPublicTrue();

        // 공개된 서평 조회
        List<Review> reviews = reviewRepository.findByIsPublicTrue();

        // 문장을 FeedItemResponse로 변환
        List<FeedItemResponse> sentenceItems = sentences.stream()
                .map(sentence -> {
                    Member author = sentence.getMember();
                    return FeedItemResponse.builder()
                            .id(sentence.getId())
                            .type("SENTENCE")
                            .content(sentence.getContent())
                            .isPublic(sentence.isPublic())
                            .createdAt(sentence.getCreatedAt())
                            .isBookmarked(bookmarkedIds.contains("SENTENCE:" + sentence.getId()))
                            .authorId(author.getId())
                            .authorNickname(author.getNickname())
                            //.authorProfileImage(author.getProfileImage()) // 프로필 이미지가 있다고 가정
                            .build();
                })
                .collect(Collectors.toList());

        List<FeedItemResponse> reviewItems = reviews.stream()
                .map(review -> {
                    Member author = review.getMember();
                    return FeedItemResponse.builder()
                            .id(review.getId())
                            .type("REVIEW")
                            .content(review.getContent())
                            .isPublic(review.isPublic())
                            .createdAt(review.getCreatedAt())
                            .isBookmarked(bookmarkedIds.contains("REVIEW:" + review.getId()))
                            .authorId(author.getId())
                            .authorNickname(author.getNickname())
                            //.authorProfileImage(author.getProfileImage())
                            .bookTitle(review.getBookTitle())
                            .bookAuthor(review.getBookAuthor())
                            .bookThumbnail(review.getBookThumbnail())
                            .isbn(review.getIsbn())
                            .build();
                })
                .collect(Collectors.toList());

        List<FeedItemResponse> allItems = new ArrayList<>();
        allItems.addAll(sentenceItems);
        allItems.addAll(reviewItems);

        allItems.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        // 페이징 처리
        int total = allItems.size();
        int start = page * size;
        int end = Math.min(start + size, total);

        if (start >= total) {
            return FeedListResponse.builder()
                    .items(Collections.emptyList())
                    .totalPages((total + size - 1) / size)
                    .totalElements(total)
                    .hasNext(false)
                    .build();
        }

        List<FeedItemResponse> pageItems = allItems.subList(start, end);

        return FeedListResponse.builder()
                .items(pageItems)
                .totalPages((total + size - 1) / size)
                .totalElements(total)
                .hasNext(end < total)
                .build();
    }


    /**
     * 북마크한 피드 목록 조회
     */
    public FeedListResponse getBookmarkedFeed(String email, int page, int size) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 북마크 목록 조회
        List<Bookmark> bookmarks = bookmarkRepository.findByMember(member);

        // 북마크한 아이템 정보 취합
        List<FeedItemResponse> items = new ArrayList<>();
        for (Bookmark bookmark : bookmarks) {
            if ("SENTENCE".equals(bookmark.getContentType())) {
                sentenceRepository.findById(bookmark.getContentId())
                        .ifPresent(sentence -> {
                            Member author = sentence.getMember();
                            items.add(FeedItemResponse.builder()
                                    .id(sentence.getId())
                                    .type("SENTENCE")
                                    .content(sentence.getContent())
                                    .isPublic(sentence.isPublic())
                                    .createdAt(sentence.getCreatedAt())
                                    .isBookmarked(true) // 북마크 목록이므로 항상 true
                                    .authorId(author.getId())
                                    .authorNickname(author.getNickname())
                                    //.authorProfileImage(author.getProfileImage())
                                    .build());
                        });
            } else if ("REVIEW".equals(bookmark.getContentType())) {
                reviewRepository.findById(bookmark.getContentId())
                        .ifPresent(review -> {
                            Member author = review.getMember();
                            items.add(FeedItemResponse.builder()
                                    .id(review.getId())
                                    .type("REVIEW")
                                    .content(review.getContent())
                                    .isPublic(review.isPublic())
                                    .createdAt(review.getCreatedAt())
                                    .isBookmarked(true) // 북마크 목록이므로 항상 true
                                    .authorId(author.getId())
                                    .authorNickname(author.getNickname())
                                    //.authorProfileImage(author.getProfileImage())
                                    .bookTitle(review.getBookTitle())
                                    .bookAuthor(review.getBookAuthor())
                                    .bookThumbnail(review.getBookThumbnail())
                                    .isbn(review.getIsbn())
                                    .build());
                        });
            }
        }

        // 최신순 정렬
        items.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        // 페이징 처리
        int total = items.size();
        int start = page * size;
        int end = Math.min(start + size, total);

        List<FeedItemResponse> pageItems = (start < total)
                ? items.subList(start, end)
                : Collections.emptyList();

        // 반환 객체 생성 및 반환
        return FeedListResponse.builder()
                .items(pageItems)
                .totalPages((total + size - 1) / size)
                .totalElements(total)
                .hasNext(end < total)
                .build();
    }

    @Transactional
    public void addBookmark(String email, String contentType, Long contentId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (bookmarkRepository.findByMemberAndContentTypeAndContentId(
                member, contentType, contentId).isPresent()) {
            throw new IllegalArgumentException("이미 북마크한 항목입니다.");
        }

        if ("SENTENCE".equals(contentType)) {
            sentenceRepository.findById(contentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문장입니다."));
        } else if ("REVIEW".equals(contentType)) {
            reviewRepository.findById(contentId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서평입니다."));
        } else {
            throw new IllegalArgumentException("유효하지 않은 컨텐츠 타입입니다.");
        }

        Bookmark bookmark = new Bookmark(member, contentType, contentId);
        bookmarkRepository.save(bookmark);
    }


    @Transactional
    public void removeBookmark(String email, String contentType, Long contentId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        bookmarkRepository.deleteByMemberAndContentTypeAndContentId(
                member, contentType, contentId);
    }
}
