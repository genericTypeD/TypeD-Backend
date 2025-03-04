package com.generic.typed.repository;

import com.generic.typed.domain.Member;
import com.generic.typed.domain.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    List<Sentence> findByMember(Member member);
    List<Sentence> findByIsPublicTrue();

    List<Sentence> findByDeviceId(String deviceId);

    Optional<Sentence> findByIdAndDeviceId(Long id, String deviceId);
}

