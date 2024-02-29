package edu.java.bot.service.processor;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.command.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMessageProcessor {
    private static final String ERROR_UNKNOWN_COMMAND_OR_INCORRECT_TEXT
        = "Sorry, you entered an unknown command or an incorrect message.";
    private final List<Command> commands;

    public List<? extends Command> commands() {
        return commands;
    }

    public SendMessage process(Update update) {
        for (Command command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }
        return new SendMessage(
            update.message().chat().id(),
            ERROR_UNKNOWN_COMMAND_OR_INCORRECT_TEXT
        );
    }
}
