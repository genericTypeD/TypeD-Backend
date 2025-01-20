package com.generic.typed.request;

import com.generic.typed.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class SentenceCreate {
    @NotBlank(message = "내용을 입력해주세요")
    private final String content;

    private boolean isPublic;


    @Builder
    public SentenceCreate(String content, boolean isPublic) {
        this.content = content;
        this.isPublic = isPublic;
    }

    public void validate() {
        if (content.contains("@@@")) {
            throw new InvalidRequest();
        }
    }
}
