package com.generic.typed.response;

import com.generic.typed.domain.Sentence;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateSentenceResponse {

    private final Long id;
    private final String content;
    private final boolean isPublic;
    private final LocalDateTime createdAt;

    public CreateSentenceResponse(Sentence sentence) {
        this.id = sentence.getId();
        this.content = sentence.getContent();
        this.isPublic = sentence.isPublic();
        this.createdAt = sentence.getCreatedAt();
    }
}
