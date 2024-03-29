package edu.java.scrapper.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        @JsonProperty("creation_date")
        Long creationDateSeconds,
        @JsonProperty("last_activity_date")
        Long lastActivityDateSeconds,
        @JsonProperty("last_edit_date")
        Long lastEditDateSeconds
    ) {
    }
}
