package edu.java.bot.service.command;

public enum CommandTitle {
    START, HELP, TRACK, UNTRACK, LIST;

    @Override
    public String toString() {
        return switch (this) {
            case START -> "/start";
            case HELP -> "/help";
            case TRACK -> "/track";
            case UNTRACK -> "/untrack";
            case LIST -> "/list";
        };
    }
}
