package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.link.Link;
import edu.java.bot.repository.link.repository.LinkRepository;
import edu.java.bot.repository.user.state.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private static final String TITLE = "/list";
    private static final String DESCRIPTION = "show the list of tracked links";
    private static final String BEGIN_OF_THE_ANSWER = "Here are the links I am tracking:";
    private static final String THERE_ARE_NO_LINKS = "Sorry, the list of tracked links is empty.";
    private static final String COMMANDS_SEPARATOR = "\n- ";
    private final LinkRepository links;
    private final UserRepository userRepository;

    @Autowired
    public ListCommand(LinkRepository links, UserRepository userRepository) {
        this.links = links;
        this.userRepository = userRepository;
    }

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
        userRepository.clearUserState(update.message().from().id());
        return new SendMessage(update.message().chat().id(), getAnswer());
    }

    private String getAnswer() {
        if (links.count() == 0) {
            return THERE_ARE_NO_LINKS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(BEGIN_OF_THE_ANSWER);
        for (Link link : links.findAll()) {
            sb.append(COMMANDS_SEPARATOR);
            sb.append(link.getUrl());
        }
        return sb.toString();
    }
}
