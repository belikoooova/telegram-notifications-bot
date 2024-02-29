package edu.java.scrapper.handler;

import edu.java.scrapper.entity.dto.ApiErrorResponse;
import edu.java.scrapper.exception.LinkAlreadyTracksException;
import java.util.Arrays;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.exception.NoSuchLinkException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LinkExceptionHandler {
    private static final String CONFLICT_DESCRIPTION = "Ссылка уже отслеживается";
    private static final String CONFLICT_CODE = "409";
    private static final String NOT_FOUND_DESCRIPTION = "Ссылка не найдена";
    private static final String NOT_FOUND_CODE = "404";

    @ExceptionHandler(LinkAlreadyTracksException.class)
    public ResponseEntity<ApiErrorResponse> handleLinkAlreadyTracksException(LinkAlreadyTracksException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                CONFLICT_DESCRIPTION,
                CONFLICT_CODE,
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
                NOT_FOUND_CODE,
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }
}