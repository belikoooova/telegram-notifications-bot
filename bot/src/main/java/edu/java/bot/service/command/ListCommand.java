package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.link.Link;
import edu.java.bot.service.link.repository.LinkRepository;
import edu.java.bot.service.user.state.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private final LinkRepository links;
    private final UserStateService userStateService;

    @Autowired
    public ListCommand(LinkRepository links, UserStateService userStateService) {
        this.links = links;
        this.userStateService = userStateService;
    }

    @Override
    public String command() {
        return CommandTitle.LIST.toString();
    }

    @Override
    public String description() {
        return CommandDescription.LIST.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        userStateService.clearUserState(update.message().from().id());
        return new SendMessage(update.message().chat().id(), getAnswer());
    }

    private String getAnswer() {
        if (links.count() == 0) {
            return CommandHumanReadableMessage.LIST_THERE_ARE_NO_LINKS.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(CommandHumanReadableMessage.LIST_BEGIN_OF_THE_ANSWER);
        for (Link link : links.findAll()) {
            sb.append(CommandHumanReadableMessage.LIST_COMMANDS_SEPARATOR);
            sb.append(link.getUrl());
        }
        return sb.toString();
    }
}
