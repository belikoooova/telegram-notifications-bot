package edu.java.scrapper.entity.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LastCommitResponse(
    Commit commit
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Commit(
        Author author
    ) {
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public record Author(
            OffsetDateTime date,
            String name
        ) {
        }
    }
}
