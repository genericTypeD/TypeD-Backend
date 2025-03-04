package com.generic.typed.dto.request;

import com.generic.typed.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class SentenceUpdateRequest {

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private boolean isPublic;

    @Builder
    public SentenceUpdateRequest(String content, boolean isPublic) {
        this.content = content;
        this.isPublic = isPublic;
    }

    public void validate() {
        if (content.contains("@@@")) {
            throw new InvalidRequest();
        }
    }
}
