package edu.java.scrapper.handler;

import edu.java.scrapper.entity.dto.ApiErrorResponse;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.exception.UnsupportedResourceException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class UpdateExceptionHandler {
    private static final String INVALID_LINK = "Error while trying to request resource";
    private static final String UNSUPPORTED_RESOURCE = "This recource is uncupported";

    @ExceptionHandler(InvalidLinkException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidLinkException(InvalidLinkException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                INVALID_LINK,
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(UnsupportedResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleUnsupportedResourceException(UnsupportedResourceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                UNSUPPORTED_RESOURCE,
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }
}
