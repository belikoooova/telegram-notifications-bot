package edu.java.bot.service.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exception.ApiErrorResponseException;
import edu.java.bot.service.command.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMessageProcessor {
    private static final String ERROR_UNKNOWN_COMMAND_OR_INCORRECT_TEXT
        = "Sorry, you entered an unknown command or an incorrect message.";
    private static final String API_ERROR_TEXT
        = "Sorry, the server message occured. Reason:\n%s";
    private final List<Command> commands;

    public List<Command> commands() {
        return commands;
    }

    public SendMessage process(Update update) {
        Long chatId = update.message().chat().id();
        try {
            for (Command command : commands) {
                if (command.supports(update)) {
                    return command.handle(update);
                }
            }
            return new SendMessage(
                chatId,
                ERROR_UNKNOWN_COMMAND_OR_INCORRECT_TEXT
            );
        } catch (ApiErrorResponseException e) {
            return new SendMessage(
                chatId,
                API_ERROR_TEXT.formatted(e.getApiErrorResponse().getDescription())
            );
        }
    }
}
