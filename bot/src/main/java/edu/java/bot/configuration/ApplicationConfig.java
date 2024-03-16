package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    Timeout timeout,
    BaseUrl baseUrl
) {
    public record Timeout(int minutes) {
    }

    public record BaseUrl(String scrapper) {
    }
}
