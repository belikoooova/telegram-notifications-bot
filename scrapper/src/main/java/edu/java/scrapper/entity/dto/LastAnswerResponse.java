package edu.java.scrapper.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LastAnswerResponse(
    List<Answer> items
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Answer(
        @JsonProperty("creation_date")
        Long creationDateSeconds
    ) {
    }
}
