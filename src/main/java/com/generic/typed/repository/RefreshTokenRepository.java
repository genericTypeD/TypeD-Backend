package com.generic.typed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.generic.typed.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByValue(String value);
}
