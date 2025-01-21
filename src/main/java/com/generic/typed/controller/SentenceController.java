package com.generic.typed.controller;

import com.generic.typed.request.SentenceCreate;
import com.generic.typed.response.CreateSentenceResponse;
import com.generic.typed.response.SentenceResponse;
import com.generic.typed.service.SentenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class SentenceController {

    private final SentenceService sentenceService;

    @PostMapping("/sentences")
    public CreateSentenceResponse write(@RequestBody @Valid SentenceCreate request) {
        request.validate();
        return sentenceService.write(request);
    }

    @GetMapping("/sentences/{sentenceId}")
    public SentenceResponse get(@PathVariable Long sentenceId) {
        SentenceResponse sentence = sentenceService.get(sentenceId);
        return sentence;
    }

}
