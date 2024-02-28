package edu.java.bot.entity.link;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public enum TrackingResource {
    GITHUB(Pattern.compile("github\\.com/(([A-Z0-9_-]+)/([A-Z0-9_-]+))/?", Pattern.CASE_INSENSITIVE)),
    STACKOVERFLOW(Pattern.compile("stackoverflow\\.com/questions/(\\d+)", Pattern.CASE_INSENSITIVE));

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

    public int idOnResourceGroupNumber() {
        return switch (this) {
            case GITHUB, STACKOVERFLOW -> 1;
        };
    }
}
