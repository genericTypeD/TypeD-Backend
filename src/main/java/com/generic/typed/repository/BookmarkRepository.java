package com.generic.typed.repository;

import com.generic.typed.domain.Bookmark;
import com.generic.typed.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMember(Member member);

    Optional<Bookmark> findByMemberAndContentTypeAndContentId(
            Member member, String contentType, Long contentId);

    void deleteByMemberAndContentTypeAndContentId(
            Member member, String contentType, Long contentId);

    long countByMember(Member member);
}