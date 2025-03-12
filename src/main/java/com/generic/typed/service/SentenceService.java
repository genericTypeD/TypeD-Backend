package com.generic.typed.service;

import com.generic.typed.domain.Member;
import com.generic.typed.domain.Sentence;
import com.generic.typed.dto.request.SentenceCreateRequest;
import com.generic.typed.dto.request.SentenceUpdateRequest;
import com.generic.typed.dto.response.MySentenceListResponse;
import com.generic.typed.dto.response.SentenceCreateResponse;
import com.generic.typed.dto.response.SentenceListResponse;
import com.generic.typed.dto.response.SentenceUpdateResponse;
import com.generic.typed.exception.SentenceNotFound;
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
    public SentenceCreateResponse createSentence(String email, SentenceCreateRequest request) {
        // 요청 검증
        request.validate();

        // 사용자 정보 조회 (로그인 필수)
        Member member = memberService.findByEmail(email);

        // 새 문장 엔티티 생성 (deviceId 없음)
        Sentence sentence = new Sentence(
                request.getContent(),
                request.isPublic(),
                LocalDateTime.now(),
                member
        );

        // 저장
        Sentence savedSentence = sentenceRepository.save(sentence);

        // 응답 생성
        return SentenceCreateResponse.from(savedSentence);
    }

    @Transactional(readOnly = true)
    public MySentenceListResponse getMySentences(String email) {
        // 회원 정보로만 조회
        Member member = memberService.findByEmail(email);
        List<Sentence> sentences = sentenceRepository.findByMember(member);

        return MySentenceListResponse.from(sentences);
    }

    @Transactional(readOnly = true)
    public SentenceListResponse getPublicSentences() {
        List<Sentence> sentences = sentenceRepository.findByIsPublicTrue();
        return SentenceListResponse.from(sentences);
    }

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