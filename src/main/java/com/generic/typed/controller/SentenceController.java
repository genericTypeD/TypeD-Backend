package com.generic.typed.controller;

import com.generic.typed.dto.request.SentenceCreateRequest;
import com.generic.typed.dto.response.MySentenceListResponse;
import com.generic.typed.dto.response.SentenceCreateResponse;
import com.generic.typed.security.jwt.util.IfLogin;
import com.generic.typed.security.jwt.util.LoginMemberDto;
import com.generic.typed.service.SentenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sentences")
public class SentenceController {
    private final SentenceService sentenceService;


    @PostMapping
    public ResponseEntity<SentenceCreateResponse> createSentence(
            @IfLogin LoginMemberDto loginMemberDto,  // required=true가 기본값
            @RequestBody SentenceCreateRequest request) {

        SentenceCreateResponse response = sentenceService.createSentence(loginMemberDto.getEmail(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("")
    public ResponseEntity<MySentenceListResponse> getMySentences(
            @IfLogin LoginMemberDto loginMemberDto) {

        MySentenceListResponse response = sentenceService.getMySentences(loginMemberDto.getEmail());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sentenceId}")
    public ResponseEntity<Void> deleteSentence(
            @IfLogin LoginMemberDto loginMemberDto,
            @PathVariable("sentenceId") Long sentenceId) {

        sentenceService.deleteSentence(loginMemberDto.getEmail(), sentenceId);
        return ResponseEntity.ok().build();
    }
}