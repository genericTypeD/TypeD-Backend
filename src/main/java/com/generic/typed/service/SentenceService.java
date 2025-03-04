package com.generic.typed.service;

import com.generic.typed.domain.Member;
import com.generic.typed.domain.Sentence;
import com.generic.typed.dto.request.SentenceCreateRequest;
import com.generic.typed.dto.response.MySentenceListResponse;
import com.generic.typed.dto.response.SentenceCreateResponse;
import com.generic.typed.dto.response.SentenceListResponse;
import com.generic.typed.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class SentenceService {
    private final SentenceRepository sentenceRepository;
    private final MemberService memberService;

    @Transactional
    public SentenceCreateResponse createSentence(String userIdentifier, SentenceCreateRequest request) {
        System.out.println("Service - UserIdentifier at start: " + userIdentifier);
        // 사용자 정보 조회 (로그인한 경우만)
        Member member = null;
        if (userIdentifier != null && userIdentifier.contains("@")) {
            try {
                // 로그인한 사용자면 조회 시도하되 예외 발생 시 무시
                member = memberService.findByEmail(userIdentifier);
            } catch (Exception e) {
                System.out.println("사용자를 찾을 수 없습니다: " + userIdentifier);
            }
        }

        // 새 문장 엔티티 생성
        Sentence sentence = new Sentence(
                request.getContent(),
                request.isPublic(),
                LocalDateTime.now(),
                member,
                member == null ? userIdentifier : null
        );

        System.out.println("Member: " + member);
        System.out.println("UserIdentifier: " + userIdentifier);
        System.out.println("Device ID being set: " + (member == null ? userIdentifier : null));

        // 저장
        Sentence savedSentence = sentenceRepository.save(sentence);

        // 응답 생성 (빌더 패턴 사용)
        return SentenceCreateResponse.from(savedSentence);
    }

    @Transactional(readOnly = true)
    public MySentenceListResponse getMySentences(String userIdentifier) {
        List<Sentence> sentences;

        if (userIdentifier.contains("@")) {
            // 이메일이면 회원 정보로 조회
            try {
                Member member = memberService.findByEmail(userIdentifier);
                sentences = sentenceRepository.findByMember(member);
            } catch (Exception e) {
                sentences = Collections.emptyList();
            }
        } else {
            // 아니면 디바이스 ID로 조회
            sentences = sentenceRepository.findByDeviceId(userIdentifier);
        }

        return MySentenceListResponse.from(sentences);
    }
    @Transactional(readOnly = true)
    public SentenceListResponse getPublicSentences() {
        List<Sentence> sentences = sentenceRepository.findByIsPublicTrue();
        return SentenceListResponse.from(sentences);
    }


//    @Transactional
//    public SentenceResponse updateSentence(String email, Long sentenceId, SentenceRequest request) {
//        Member member = memberService.findByEmail(email);
//        Sentence sentence = sentenceRepository.findById(sentenceId)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 문장입니다."));
//
//        if (!sentence.getMember().equals(member)) {
//            throw new RuntimeException("해당 문장을 수정할 권한이 없습니다.");
//        }
//
//        sentence.update(request.getContent(), request.isPublic());
//        return SentenceResponse.from(sentence);
//    }

    @Transactional
    public void deleteSentence(String email, Long sentenceId) {
        Member member = memberService.findByEmail(email);
        Sentence sentence = sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 문장입니다."));

        if (!sentence.getMember().equals(member)) {
            throw new RuntimeException("해당 문장을 삭제할 권한이 없습니다.");
        }

        sentenceRepository.delete(sentence);
    }
}