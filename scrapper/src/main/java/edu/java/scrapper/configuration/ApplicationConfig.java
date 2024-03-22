package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    LinkUpdaterSchedulerRecord linkUpdaterScheduler,
    BaseUrl baseUrl,
    Timeout timeout
) {
    public record LinkUpdaterSchedulerRecord(@NotNull Duration interval) {
    }

    public record BaseUrl(String gitHub, String stackOverflow, String bot) {
    }

    public record Timeout(int minutes) {
    }
}
