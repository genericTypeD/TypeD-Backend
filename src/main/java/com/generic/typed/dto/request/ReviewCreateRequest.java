package com.generic.typed.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ReviewCreateRequest {
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
    @JsonProperty("isPublic")
    private boolean isPublic;

    private String bookIsbn;

    private String bookTitle;

    private String thumbnail;

    @Builder
    public ReviewCreateRequest(String content, boolean isPublic, String bookIsbn,
                               String bookTitle, String thumbnail) {
        this.content = content;
        this.isPublic = isPublic;
        this.bookIsbn = bookIsbn;
        this.bookTitle = bookTitle;
        this.thumbnail = thumbnail;
    }

    public void validate() {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("서평 내용을 입력해주세요");
        }

        if (bookTitle == null || bookTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("책 제목을 입력해주세요");
        }
    }
}