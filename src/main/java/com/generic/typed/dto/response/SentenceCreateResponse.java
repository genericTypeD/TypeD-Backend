package com.generic.typed.dto.response;

import com.generic.typed.domain.Sentence;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SentenceCreateResponse {
    private final Long sentenceId;
    private final String content;
    private final boolean isPublic;
    private final LocalDateTime createdAt;

    // 정적 팩토리 메서드 추가
    public static SentenceCreateResponse from(Sentence sentence) {
        return SentenceCreateResponse.builder()
                .sentenceId(sentence.getId())
                .content(sentence.getContent())
                .isPublic(sentence.isPublic())
                .createdAt(sentence.getCreatedAt())
                .build();
    }
}