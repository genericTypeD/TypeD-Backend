package com.generic.typed.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private boolean isPublic;

    private LocalDateTime createdAt;
    @Builder
    public Sentence(String content, boolean isPublic) {
        this.content = content;
        this.isPublic = isPublic;
        this.createdAt = LocalDateTime.now();
    }
}
