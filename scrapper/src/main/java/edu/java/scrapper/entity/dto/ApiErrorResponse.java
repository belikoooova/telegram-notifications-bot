package edu.java.scrapper.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ApiErrorResponse {
    private final String description;
    private final String code;
    private final String exceptionName;
    private final String exceptionMessage;
    private final List<String> stacktrace;
}
