package ru.university.universityutils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.university.universityutils.exceptions.custom_exception.EntityNotFoundException;
import ru.university.universityutils.exceptions.custom_exception.FileStorageException;
import ru.university.universityutils.exceptions.custom_exception.RestRuntimeException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ControllerAdvice(basePackages =
        {"ru.university.universityteacher.controller",
                "ru.university.universitystudent.controller"})
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> entityNotFound(EntityNotFoundException ex) {

        ApiErrorResponse apiResponse = new ApiErrorResponse.ApiErrorResponseBuilder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withErrorCode("404")
                .withMessage(ex.getMessage())
                .withDetails("Невалидный id")
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return new ResponseEntity<ApiErrorResponse>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RestRuntimeException.class, FileStorageException.class})
    public ResponseEntity<ApiErrorResponse> handleRestRuntimeException(RuntimeException ex) {

        ApiErrorResponse apiResponse = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .withErrorCode("500")
                .withMessage(ex.getMessage())
                .withDetails(String.valueOf(ex.getCause()))
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return new ResponseEntity<ApiErrorResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiErrorResponse> handleDefaultException(Exception ex, HttpStatus httpStatus) {

        ApiErrorResponse apiResponse = new ApiErrorResponse
                .ApiErrorResponseBuilder()
                .withStatus(httpStatus)
                .withErrorCode("503")
                .withMessage(ex.getLocalizedMessage())
                .withDetails("Что-то пошло не так")
                .atTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
        return new ResponseEntity<ApiErrorResponse>(apiResponse, apiResponse.getHttpStatus());
    }
}