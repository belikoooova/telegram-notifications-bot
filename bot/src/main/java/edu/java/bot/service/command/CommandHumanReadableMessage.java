package edu.java.bot.service.command;

public enum CommandHumanReadableMessage {
    TRACK_GET_URL,
    TRACK_OK,
    UNTRACK_GET_URL,
    UNTRACK_OK,
    START_ANSWER,
    HELP_BEGIN_OF_THE_ANSWER,
    HELP_COMMANDS_SEPARATOR,
    HELP_LINES_SEPARATOR,
    LIST_BEGIN_OF_THE_ANSWER,
    LIST_THERE_ARE_NO_LINKS,
    LIST_COMMANDS_SEPARATOR,
    LINK_NOT_OK;

    @Override
    public String toString() {
        return switch (this) {
            case TRACK_GET_URL -> "Enter the link you want to start tracking.";
            case UNTRACK_GET_URL -> "Enter the link you want to stop tracking.";
            case START_ANSWER -> "Hello! This bot will help you track updates on the resources you need "
                + "(currently supports StackOverflow and GitHub). Type /help to see the list of commands.";
            case HELP_BEGIN_OF_THE_ANSWER -> "Here are the commands I can perform:\n";
            case HELP_COMMANDS_SEPARATOR -> " -> ";
            case HELP_LINES_SEPARATOR -> ";\n";
            case LIST_BEGIN_OF_THE_ANSWER -> "Here are the links I am tracking:";
            case LIST_THERE_ARE_NO_LINKS -> "Sorry, the list of tracked links is empty.";
            case TRACK_OK -> "The link was successfully added.";
            case UNTRACK_OK -> "The link was successfully deleted.";
            case LIST_COMMANDS_SEPARATOR -> "\n- ";
            case LINK_NOT_OK -> "Sorry, your link is incorrect. Please try again or choose another command.";
        };
    }
}
