package edu.java.bot.service.validation;

import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TrackingResource {
    GITHUB(Pattern.compile("github\\.com/(([A-Z0-9_-]+)/([A-Z0-9_-]+))/?", Pattern.CASE_INSENSITIVE)),
    STACKOVERFLOW(Pattern.compile("stackoverflow\\.com/(questions/\\d+)", Pattern.CASE_INSENSITIVE));

    private final Pattern regex;

    @Override
    public String toString() {
        return switch (this) {
            case GITHUB -> "GitHub";
            case STACKOVERFLOW -> "StackOverflow";
        };
    }

    public String baseUrl() {
        return switch (this) {
            case GITHUB -> "https://github.com/";
            case STACKOVERFLOW -> "https://stackoverflow.com/";
        };
    }

    public int pathOnResourceGroupNumber() {
        return switch (this) {
            case GITHUB, STACKOVERFLOW -> 1;
        };
    }
}
