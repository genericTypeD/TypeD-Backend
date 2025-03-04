package com.generic.typed.dto.response;

import com.generic.typed.domain.Sentence;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MySentenceListResponse {
    private final List<SentenceResponse> sentences;

    @Builder
    public MySentenceListResponse(List<SentenceResponse> sentences) {
        this.sentences = sentences;
    }

    public static MySentenceListResponse from(List<Sentence> sentences) {
        List<SentenceResponse> sentenceResponses = sentences.stream()
                .map(SentenceResponse::from)
                .collect(Collectors.toList());

        return MySentenceListResponse.builder()
                .sentences(sentenceResponses)
                .build();
    }
}
