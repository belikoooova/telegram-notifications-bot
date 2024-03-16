package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.repository.chat.ChatRepository;
import java.util.List;
import edu.java.bot.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {
    private static final String TITLE = "/help";
    private static final String DESCRIPTION = "show all commands";
    private static final String BEGIN_OF_THE_ANSWER = "Here are the commands I can perform:\n";
    private static final String COMMANDS_SEPARATOR = " -> ";
    private static final String LINES_SEPARATOR = ";\n";

    private final List<Command> commands;
    // private final ChatRepository chatRepository;
    private final ChatService chatService;

    @Override
    public String command() {
        return TITLE;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        chatService.setChatState(chatId, ChatState.NONE);
        return new SendMessage(chatId, getAnswer());
    }

    private String getAnswer() {
        StringBuilder sb = new StringBuilder();
        sb.append(BEGIN_OF_THE_ANSWER);
        for (int i = 0; i < commands.size(); ++i) {
            sb.append(commands.get(i).command());
            sb.append(COMMANDS_SEPARATOR);
            sb.append(commands.get(i).description());
            if (i != commands.size() - 1) {
                sb.append(LINES_SEPARATOR);
            }
        }
        return sb.toString();
    }
}
