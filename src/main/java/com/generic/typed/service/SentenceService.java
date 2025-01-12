package com.generic.typed.service;

import com.generic.typed.domain.Sentence;
import com.generic.typed.repository.SentenceRepository;
import com.generic.typed.request.SentenceCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    public void write(SentenceCreate sentenceCreate) {
        Sentence sentence = Sentence.builder()
                .content(sentenceCreate.getContent())
                .isPublic(sentenceCreate.isPublic())
                .build();
    }

}
