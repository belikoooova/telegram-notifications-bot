package edu.java.scrapper.service.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    @JsonProperty("owner") UserResponse owner,
    @JsonProperty("name") String title,
    @JsonProperty("created_at") OffsetDateTime createdTime,
    @JsonProperty("updated_at") OffsetDateTime updatedTime,
    @JsonProperty("pushed_at") OffsetDateTime pushedTime
) {
    public record UserResponse(
        @JsonProperty("login") String login
    ) { }
}
