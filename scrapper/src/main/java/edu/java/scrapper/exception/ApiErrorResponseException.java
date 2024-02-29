package edu.java.scrapper.exception;

import edu.java.scrapper.entity.dto.ApiErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class ApiErrorResponseException extends RuntimeException {
    @NotNull private final ApiErrorResponse apiErrorResponse;
}
