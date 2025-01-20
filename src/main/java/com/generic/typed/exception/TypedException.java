package com.generic.typed.exception;

public abstract class TypedException extends RuntimeException{

    public TypedException(String message) {
        super(message);
    }

    public TypedException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
