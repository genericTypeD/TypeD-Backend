package com.generic.typed.dto.response;

import com.generic.typed.domain.Sentence;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SentenceResponse {
    private final Long id;
    private final String content;
    private final boolean isPublic;
    private final LocalDateTime createdAt;

    @Builder
    public SentenceResponse(Long id, String content, boolean isPublic, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

    public static SentenceResponse from(Sentence sentence) {
        return SentenceResponse.builder()
                .id(sentence.getId())
                .content(sentence.getContent())
                .isPublic(sentence.isPublic())
                .createdAt(sentence.getCreatedAt())
                .build();
    }
}
