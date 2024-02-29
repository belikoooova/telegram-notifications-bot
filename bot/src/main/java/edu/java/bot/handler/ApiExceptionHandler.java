package edu.java.bot.handler;

import edu.java.bot.entity.dto.ApiErrorResponse;
import edu.java.bot.exception.NoExistingChatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final String BAD_REQUEST_DESCRIPTION = "Некорректные параметры запроса";
    private static final String BAD_REQUEST_CODE = "400";
    private static final String NOT_FOUND_DESCRIPTION = "Чат %d не найден";
    private static final String NOT_FOUND_CODE = "404";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(BAD_REQUEST_DESCRIPTION,
                BAD_REQUEST_CODE,
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }

    @ExceptionHandler(NoExistingChatException.class)
    public ResponseEntity<ApiErrorResponse> handleNoExistingChatException(NoExistingChatException e) {
        return ResponseEntity.badRequest()
            .body(new ApiErrorResponse(NOT_FOUND_DESCRIPTION.formatted(e.getChatId()),
                NOT_FOUND_CODE,
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList()
            ));
    }
}
