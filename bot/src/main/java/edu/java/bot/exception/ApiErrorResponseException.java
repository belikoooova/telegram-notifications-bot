package edu.java.bot.exception;

import edu.java.bot.entity.dto.ApiErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class ApiErrorResponseException extends RuntimeException {
    @NotNull private ApiErrorResponse apiErrorResponse;
}
