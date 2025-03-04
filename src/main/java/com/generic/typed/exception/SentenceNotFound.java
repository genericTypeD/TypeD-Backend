package com.generic.typed.exception;

public class SentenceNotFound extends TypedException {

    private static final String MESSAGE = "존재하지 않는 문장입니다.";

    public SentenceNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
