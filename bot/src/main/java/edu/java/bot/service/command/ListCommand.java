package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.link.Link;
import edu.java.bot.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private static final String TITLE = "/list";
    private static final String DESCRIPTION = "show the list of tracked links";
    private static final String BEGIN_OF_THE_ANSWER = "Here are the links I am tracking:";
    private static final String THERE_ARE_NO_LINKS = "Sorry, the list of tracked links is empty.";
    private static final String COMMANDS_SEPARATOR = "\n- ";
    private final UserRepository userRepository;

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
        Long userId = update.message().from().id();
        userRepository.clearUserState(userId);
        return new SendMessage(update.message().chat().id(), getAnswer(userId));
    }

    private String getAnswer(Long userId) {
        if (userRepository.getUserLinks(userId).isEmpty()) {
            return THERE_ARE_NO_LINKS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(BEGIN_OF_THE_ANSWER);
        for (Link link : userRepository.getUserLinks(userId)) {
            sb.append(COMMANDS_SEPARATOR);
            sb.append(link.getUrl());
        }
        return sb.toString();
    }
}
