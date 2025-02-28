package com.generic.typed.request;

import com.generic.typed.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ReviewCreateRequest {
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private boolean isPublic;

    private String isbn;

    @Builder
    public ReviewCreateRequest(String content, boolean isPublic, String isbn) {
        this.content = content;
        this.isPublic = isPublic;
        this.isbn = isbn;
    }

    public void validate() {
        if (content.contains("@@@")) {
            throw new InvalidRequest();
        }
    }
}