package ru.university.universityutils.exceptions;

import com.google.common.io.CharStreams;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

@Component
public class FeignExceptionHandler implements ErrorDecoder {

    private final ErrorDecoder.Default defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404: {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, readBody(response));
            }
            case 500: {
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, readBody(response));
            }
        }
        return defaultDecoder.decode(methodKey, response);
    }

    private String readBody(Response response) {
        String message = null;
        try (Reader reader = response.body().asReader()) {

            message = CharStreams.toString(reader);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return message;
    }
}
