package edu.java.bot.service.processor;

public enum ProcessorHumanReadableMessage {
    ERROR_UNKNOWN_COMMAND_OR_INCORRECT_TEXT;

    @Override
    public String toString() {
        return switch (this) {
            case ERROR_UNKNOWN_COMMAND_OR_INCORRECT_TEXT ->
                "Sorry, you entered an unknown command or an incorrect message.";
        };
    }
}
