package com.generic.typed.repository;

import com.generic.typed.domain.Member;
import com.generic.typed.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMember(Member member);
    List<Review> findByIsPublicTrue();
    List<Review> findByDeviceId(String deviceId);
    Optional<Review> findByIdAndDeviceId(Long id, String deviceId);
}