package edu.java.scrapper.entity.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
