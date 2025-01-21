package com.generic.typed.response;

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
}
