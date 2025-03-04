package com.generic.typed.dto.response;

import com.generic.typed.domain.Sentence;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SentenceListResponse {
    private final List<SentenceResponse> sentences;

    @Builder
    public SentenceListResponse(List<SentenceResponse> sentences) {
        this.sentences = sentences;
    }

    public static SentenceListResponse from(List<Sentence> sentences) {
        List<SentenceResponse> sentenceResponses = sentences.stream()
                .map(SentenceResponse::from)
                .collect(Collectors.toList());

        return SentenceListResponse.builder()
                .sentences(sentenceResponses)
                .build();
    }
}