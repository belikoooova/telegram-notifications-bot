package edu.java.scrapper.handler;

import edu.java.scrapper.entity.dto.ApiErrorResponse;
import edu.java.scrapper.exception.ChatAlreadyExistsException;
import edu.java.scrapper.exception.NoSuchChatException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChatExceptionHandler {
    private static final String BAD_REQUEST_DESCRIPTION = "Incorrect request parameters";
    private static final String NOT_FOUND_DESCRIPTION = "Chat %d not founded";
    private static final String CONFLICT_DESCRIPTION = "Chat %d already exists";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                BAD_REQUEST_DESCRIPTION,
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(NoSuchChatException.class)
    public ResponseEntity<ApiErrorResponse> handleNoExistingChatException(NoSuchChatException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                NOT_FOUND_DESCRIPTION.formatted(e.getChatId()),
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(ChatAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleChatAlreadyExistsException(ChatAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                CONFLICT_DESCRIPTION.formatted(e.getChatId()),
                String.valueOf(HttpStatus.CONFLICT.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }
}
