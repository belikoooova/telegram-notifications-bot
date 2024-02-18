package edu.java.bot.service.link;

import java.util.regex.Pattern;

public enum LinkResource {
    GITHUB,
    STACKOVERFLOW;

    @Override
    public String toString() {
        return switch (this) {
            case GITHUB -> "GitHub";
            case STACKOVERFLOW -> "StackOverflow";
        };
    }

    public Pattern regex() {
        return switch (this) {
            case GITHUB -> Pattern.compile("github\\.com/([A-Z0-9_-]+/[A-Z0-9_-]+)", Pattern.CASE_INSENSITIVE);
            case STACKOVERFLOW -> Pattern.compile("stackoverflow\\.com/questions/(\\d+)", Pattern.CASE_INSENSITIVE);
        };
    }

    public int idOnResourceGroupNumber() {
        return switch (this) {
            case GITHUB, STACKOVERFLOW -> 1;
        };
    }
}
