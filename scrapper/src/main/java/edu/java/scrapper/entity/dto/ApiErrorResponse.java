package edu.java.scrapper.entity.dto;

import java.util.List;
import lombok.Data;

@Data
public class ApiErrorResponse {
    private final String description;
    private final String code;
    private final String exceptionName;
    private final String exceptionMessage;
    private final List<String> stacktrace;
}
