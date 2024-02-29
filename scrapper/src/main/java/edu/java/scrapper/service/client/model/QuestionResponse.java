package edu.java.scrapper.service.client.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record QuestionResponse(
    List<ItemResponse> items
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ItemResponse(
        Long questionId,
        Long creationDateSeconds,
        Long lastActivityDateSeconds,
        Long lastEditDateSeconds
    ) {
    }
}
