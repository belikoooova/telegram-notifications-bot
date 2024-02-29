package edu.java.scrapper.handler;

import edu.java.scrapper.entity.dto.ApiErrorResponse;
import edu.java.scrapper.exception.ChatAlreadyExistsException;
import edu.java.scrapper.exception.NoSuchChatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ChatExceptionHandler {
    private static final String BAD_REQUEST_DESCRIPTION = "Некорректные параметры запроса";
    private static final String BAD_REQUEST_CODE = "400";
    private static final String NOT_FOUND_DESCRIPTION = "Чат %d не найден";
    private static final String NOT_FOUND_CODE = "404";
    private static final String CONFLICT_DESCRIPTION = "Чат %d уже существует";
    private static final String CONFLICT_CODE = "409";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(
                BAD_REQUEST_DESCRIPTION,
                BAD_REQUEST_CODE,
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
                NOT_FOUND_CODE,
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
                CONFLICT_CODE,
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }
}
