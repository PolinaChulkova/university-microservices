package ru.university.universityutils.exceptions.custom_exception;

public class RestRuntimeException extends RuntimeException {
    public RestRuntimeException() {
        super();
    }

    public RestRuntimeException(String message) {
        super(message);
    }

    public RestRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
