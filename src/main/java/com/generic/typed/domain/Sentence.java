package com.generic.typed.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false)
    private boolean isPublic;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // deviceId 필드 제거

    public Sentence(String content, boolean isPublic, LocalDateTime createdAt, Member member) {
        this.content = content;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.member = member;
    }

    public void update(String content, boolean isPublic) {
        this.content = content;
        this.isPublic = isPublic;
    }
}
