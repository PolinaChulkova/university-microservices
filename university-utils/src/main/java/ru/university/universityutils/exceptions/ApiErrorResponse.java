package ru.university.universityutils.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter@Setter
public class ApiErrorResponse {

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
    private String details;
    private LocalDateTime timeStamp;

    public static final class ApiErrorResponseBuilder {

        private HttpStatus httpStatus;
        private String errorCode;
        private String message;
        private String details;
        private LocalDateTime timeStamp;

        public ApiErrorResponseBuilder() {
        }

        public ApiErrorResponseBuilder withStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ApiErrorResponseBuilder withErrorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ApiErrorResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorResponseBuilder withDetails(String details) {
            this.details = details;
            return this;
        }

        public ApiErrorResponseBuilder atTime(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public ApiErrorResponse build() {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
            apiErrorResponse.httpStatus = this.httpStatus;
            apiErrorResponse.errorCode = this.errorCode;
            apiErrorResponse.message = this.message;
            apiErrorResponse.details = this.details;
            apiErrorResponse.timeStamp = this.timeStamp;
            return apiErrorResponse;
        }
    }
}
