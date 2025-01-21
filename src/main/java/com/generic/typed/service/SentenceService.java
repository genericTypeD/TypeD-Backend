package com.generic.typed.service;

import com.generic.typed.domain.Sentence;
import com.generic.typed.exception.SentenceNotFound;
import com.generic.typed.repository.SentenceRepository;
import com.generic.typed.request.SentenceCreate;
import com.generic.typed.response.CreateSentenceResponse;
import com.generic.typed.response.SentenceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    public CreateSentenceResponse write(SentenceCreate sentenceCreate) {
        Sentence sentence = Sentence.builder()
                .content(sentenceCreate.getContent())
                .isPublic(sentenceCreate.isPublic())
                .build();

        Sentence savedSentence = sentenceRepository.save(sentence);
        return new CreateSentenceResponse(savedSentence);
    }

    public SentenceResponse get(Long id) {
        Sentence sentence = sentenceRepository.findById(id)
                .orElseThrow(()-> new SentenceNotFound());

        return SentenceResponse.builder()
                .id(sentence.getId())
                .content(sentence.getContent())
                .isPublic(sentence.isPublic())
                .createdAt(sentence.getCreatedAt())
                .build();

    }
}
