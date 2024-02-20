package edu.java.scrapper.service.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record QuestionResponse(
    @JsonProperty("items") List<ItemResponse> items
) {
    public record ItemResponse(
        @JsonProperty("question_id") Long questionId,
        @JsonProperty("creation_date") Long creationDateSeconds,
        @JsonProperty("last_activity_date") Long lastActivityDateSeconds,
        @JsonProperty("last_edit_date") Long lastEditDateSeconds
    ) { }
}
