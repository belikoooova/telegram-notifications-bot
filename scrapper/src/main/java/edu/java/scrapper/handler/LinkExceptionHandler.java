package edu.java.scrapper.handler;

import edu.java.scrapper.entity.dto.ApiErrorResponse;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotTrackedException;
import edu.java.scrapper.exception.NoSuchLinkException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LinkExceptionHandler {
    private static final String CONFLICT_DESCRIPTION = "Link is already being tracked";
    private static final String NOT_FOUND_DESCRIPTION = "Link not found";
    private static final String NOT_TRACKED_DESCRIPTION = "Link not tracked";

    @ExceptionHandler(LinkAlreadyTrackedException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkAlreadyTracksException(LinkAlreadyTrackedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                CONFLICT_DESCRIPTION,
                String.valueOf(HttpStatus.CONFLICT.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(NoSuchLinkException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchLinkException(NoSuchLinkException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                NOT_FOUND_DESCRIPTION,
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(LinkNotTrackedException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkNotTrackedException(LinkNotTrackedException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                NOT_TRACKED_DESCRIPTION,
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }
}
