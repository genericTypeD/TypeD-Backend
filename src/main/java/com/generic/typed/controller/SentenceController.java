package com.generic.typed.controller;

import com.generic.typed.exception.InvalidRequest;
import com.generic.typed.request.SentenceCreate;
import com.generic.typed.service.SentenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SentenceController {

    private final SentenceService sentenceService;

    @PostMapping("/sentences")
    public void write(@RequestBody @Valid SentenceCreate request) {
        request.validate();
        sentenceService.write(request);
    }
}
