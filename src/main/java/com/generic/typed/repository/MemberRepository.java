package com.generic.typed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.generic.typed.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);
}
