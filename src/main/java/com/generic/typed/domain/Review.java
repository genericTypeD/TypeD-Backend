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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private boolean isPublic;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(length = 100)
    private String deviceId;

    // 책 관련 정보
    @Column(length = 200)
    private String bookTitle;

    @Column(length = 100)
    private String bookAuthor;

    @Column(length = 500)
    private String bookThumbnail;

    @Column(length = 20)
    private String isbn;

    public Review(String content, boolean isPublic, LocalDateTime createdAt, Member member, String deviceId,
                  String bookTitle, String bookAuthor, String bookThumbnail, String isbn) {
        this.content = content;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.member = member;
        this.deviceId = deviceId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookThumbnail = bookThumbnail;
        this.isbn = isbn;
    }

    public void update(String content, boolean isPublic) {
        this.content = content;
        this.isPublic = isPublic;
    }
}