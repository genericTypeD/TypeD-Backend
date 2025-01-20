package com.generic.typed.exception;

public class InvalidRequest extends TypedException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
