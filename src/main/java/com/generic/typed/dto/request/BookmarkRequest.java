package com.generic.typed.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkRequest {
    private String contentType; // "SENTENCE" 또는 "REVIEW"
    private Long contentId;
}
