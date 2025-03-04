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



    @Transactional
    public SentenceUpdateResponse updateSentence(String userIdentifier, Long sentenceId, SentenceUpdateRequest request) {
        // 요청 검증
        if (userIdentifier == null) {
            userIdentifier = "anonymous";
        }

        Sentence sentence;

        // 사용자 식별자가 이메일인 경우 (로그인한 사용자)
        if (userIdentifier.contains("@")) {
            try {
                Member member = memberService.findByEmail(userIdentifier);
                sentence = sentenceRepository.findById(sentenceId)
                        .orElseThrow(() -> new SentenceNotFound());

                // 해당 문장의 소유자가 맞는지 확인
                if (!sentence.getMember().equals(member)) {
                    throw new IllegalArgumentException("해당 문장을 수정할 권한이 없습니다.");
                }
            } catch (SentenceNotFound e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalArgumentException("문장 수정 중 오류가 발생했습니다.");
            }
        } else {
            // 디바이스 ID로 검색 (비로그인 사용자)
            sentence = sentenceRepository.findByIdAndDeviceId(sentenceId, userIdentifier)
                    .orElseThrow(() -> new SentenceNotFound());
        }

        // 문장 내용 및 공개여부 업데이트
        sentence.update(request.getContent(), request.isPublic());

        // 응답 생성
        return SentenceUpdateResponse.from(sentence);
    }

    @Transactional
    public void deleteSentence(String userIdentifier, Long sentenceId) {
        Sentence sentence;

        // 사용자 식별자가 이메일인 경우 (로그인한 사용자)
        if (userIdentifier.contains("@")) {
            try {
                Member member = memberService.findByEmail(userIdentifier);
                sentence = sentenceRepository.findById(sentenceId)
                        .orElseThrow(() -> new SentenceNotFound());

                // 해당 문장의 소유자가 맞는지 확인
                if (!sentence.getMember().equals(member)) {
                    throw new IllegalArgumentException("해당 문장을 삭제할 권한이 없습니다.");
                }
            } catch (SentenceNotFound e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalArgumentException("문장 삭제 중 오류가 발생했습니다.");
            }
        } else {
            // 디바이스 ID로 검색 (비로그인 사용자)
            sentence = sentenceRepository.findByIdAndDeviceId(sentenceId, userIdentifier)
                    .orElseThrow(() -> new SentenceNotFound());
        }

        // 문장 삭제
        sentenceRepository.delete(sentence);
    }


}