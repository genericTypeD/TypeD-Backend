package com.generic.typed.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 북마크한 사용자

    @Column(nullable = false)
    private String contentType; // "SENTENCE" 또는 "REVIEW"

    @Column(nullable = false)
    private Long contentId; // 문장 또는 서평의 ID

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Bookmark(Member member, String contentType, Long contentId) {
        this.member = member;
        this.contentType = contentType;
        this.contentId = contentId;
    }
}
