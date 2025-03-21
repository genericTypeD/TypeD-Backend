package com.generic.typed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedItemResponse {
    // 공통 필드
    private Long id;
    private String type; // "SENTENCE" 또는 "REVIEW"
    private String content;
    private boolean isPublic;
    private LocalDateTime createdAt;
    private boolean isBookmarked;

    // 작성자 정보
    private Long authorId;
    private String authorNickname;
    private String authorProfileImage;

    // 서평 추가 정보 (type이 "REVIEW"인 경우)
    private String bookTitle;
    private String bookAuthor;
    private String bookThumbnail;
    private String isbn;
}
