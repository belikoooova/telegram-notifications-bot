package edu.java.scrapper.service.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RepositoryResponse(
    UserResponse owner,
    String name,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    OffsetDateTime pushedAt
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record UserResponse(
        String login
    ) { }
}
