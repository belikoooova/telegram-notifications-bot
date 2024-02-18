package edu.java.bot.service.command;

public enum CommandDescription {
    START, HELP, TRACK, UNTRACK, LIST;

    @Override
    public String toString() {
        return switch (this) {
            case START -> "register user";
            case HELP -> "show all commands";
            case TRACK -> "start tracking a link";
            case UNTRACK -> "stop tracking a link";
            case LIST -> "show the list of tracked links";
        };
    }
}
