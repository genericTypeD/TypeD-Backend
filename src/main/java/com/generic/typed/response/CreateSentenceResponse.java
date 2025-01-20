package com.generic.typed.response;

import lombok.Getter;

@Getter
public class CreateSentenceResponse {

    private final Long id;

    public CreateSentenceResponse(Long id) {
        this.id = id;
    }
}
