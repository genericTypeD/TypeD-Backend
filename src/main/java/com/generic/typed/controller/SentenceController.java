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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sentences")
public class SentenceController {
    private final SentenceService sentenceService;

    /**
     * 문장 등록 - 로그인 불필요
     * 로그인하지 않은 사용자는 deviceId로 구분
     */
    @PostMapping
    public ResponseEntity<SentenceCreateResponse> createSentence(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @IfLogin(required = false) LoginMemberDto loginMemberDto,
            @RequestBody SentenceCreateRequest request) {

        System.out.println("Controller - X-Device-Id Header: " + deviceId);
        System.out.println("Controller - LoginMemberDto: " + loginMemberDto);

        // 로그인한 경우 email 사용, 비로그인 시 deviceId 사용
        // 수정된 조건문
        String userIdentifier = (loginMemberDto != null && loginMemberDto.getEmail() != null)
                ? loginMemberDto.getEmail()
                : (deviceId != null ? deviceId : UUID.randomUUID().toString());

        System.out.println("Controller - UserIdentifier before service call: " + userIdentifier);

        SentenceCreateResponse response = sentenceService.createSentence(userIdentifier, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 내 문장 목록 조회 - 로그인 불필요
     * deviceId로 문장 소유자 구분
     */
    @GetMapping("")
    public ResponseEntity<MySentenceListResponse> getMySentences(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @IfLogin(required = false) LoginMemberDto loginMemberDto) {

        String userIdentifier = loginMemberDto != null && loginMemberDto.getEmail() != null
                ? loginMemberDto.getEmail()
                : (deviceId != null ? deviceId : "anonymous");

        MySentenceListResponse response = sentenceService.getMySentences(userIdentifier);
        return ResponseEntity.ok(response);
    }

//    /**
//     * 공개 문장 목록 조회 (피드) - 로그인 필요
//     */
//    @GetMapping("/feed")
//    public ResponseEntity<SentenceListResponse> getFeed(@IfLogin LoginMemberDto loginMemberDto) {
//        SentenceListResponse response = sentenceService.getPublicSentences();
//        return ResponseEntity.ok(response);
//    }


    /**
     * 문장 수정 - 로그인 불필요, deviceId로 소유자 확인
     */
//    @PutMapping("/{sentenceId}")
//    public ResponseEntity<SentenceResponse> updateSentence(
//            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
//            @IfLogin(required = false) LoginMemberDto loginMemberDto,
//            @PathVariable("sentenceId") Long sentenceId,
//            @RequestBody SentenceRequest request) {
//
//        String userIdentifier = loginMemberDto != null
//                ? loginMemberDto.getEmail()
//                : (deviceId != null ? deviceId : "anonymous");
//
//        SentenceResponse response = sentenceService.updateSentence(userIdentifier, sentenceId, request);
//        return ResponseEntity.ok(response);
//    }

    /**
     * 문장 삭제 - 로그인 불필요, deviceId로 소유자 확인
     */
    @DeleteMapping("/{sentenceId}")
    public ResponseEntity<Void> deleteSentence(
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            @IfLogin(required = false) LoginMemberDto loginMemberDto,
            @PathVariable("sentenceId") Long sentenceId) {

        String userIdentifier = loginMemberDto != null
                ? loginMemberDto.getEmail()
                : (deviceId != null ? deviceId : "anonymous");

        sentenceService.deleteSentence(userIdentifier, sentenceId);
        return ResponseEntity.ok().build();
    }

}
