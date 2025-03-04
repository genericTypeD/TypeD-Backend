package com.generic.typed.dto.response;

import com.generic.typed.domain.Sentence;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SentenceUpdateResponse {
    private final Long id;
    private final String content;
    private final boolean isPublic;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public SentenceUpdateResponse(Long id, String content, boolean isPublic, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SentenceUpdateResponse from(Sentence sentence) {
        return SentenceUpdateResponse.builder()
                .id(sentence.getId())
                .content(sentence.getContent())
                .isPublic(sentence.isPublic())
                .createdAt(sentence.getCreatedAt())
                .updatedAt(sentence.getUpdatedAt())
                .build();
    }
}