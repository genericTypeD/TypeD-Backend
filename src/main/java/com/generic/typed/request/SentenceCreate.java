package com.generic.typed.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class SentenceCreate {

    private String content;

    private boolean isPublic;

    private LocalDateTime createdAt;

    public SentenceCreate(String content, boolean isPublic, LocalDateTime createdAt) {
        this.content = content;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

}
