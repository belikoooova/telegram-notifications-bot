package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.link.factory.LinkFactory;
import edu.java.bot.service.link.repository.LinkRepository;
import edu.java.bot.service.link.repository.LinkRepositoryKey;
import edu.java.bot.service.user.state.UserState;
import edu.java.bot.service.user.state.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    private boolean isAwaiting = false;
    private final LinkRepository linkRepository;
    private final UserStateService userStateService;
    private final LinkFactory linkFactory;

    @Autowired
    public UntrackCommand(LinkRepository linkRepository, UserStateService userStateService, LinkFactory linkFactory) {
        this.linkRepository = linkRepository;
        this.userStateService = userStateService;
        this.linkFactory = linkFactory;
    }

    @Override
    public String command() {
        return CommandTitle.UNTRACK.toString();
    }

    @Override
    public String description() {
        return CommandDescription.UNTRACK.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        Long userId = update.message().from().id();
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        if (isAwaiting && userStateService.getUserState(userId).equals(UserState.AWAITING_URL)) {
            try {
                LinkRepositoryKey key = linkFactory.createKey(userId, text);
                linkRepository.deleteById(key);
                isAwaiting = false;
                userStateService.clearUserState(userId);
                return new SendMessage(chatId, CommandHumanReadableMessage.UNTRACK_OK.toString());
            } catch (IllegalArgumentException e) {
                return new SendMessage(chatId, CommandHumanReadableMessage.LINK_NOT_OK.toString());
            }
        }
        isAwaiting = true;
        userStateService.setUserState(userId, UserState.AWAITING_URL);
        return new SendMessage(chatId, CommandHumanReadableMessage.TRACK_GET_URL.toString());
    }

    @Override
    public boolean supports(Update update) {
        Long userId = update.message().from().id();
        if (isAwaiting && userStateService.getUserState(userId).equals(UserState.AWAITING_URL)) {
            return true;
        }
        return update.message().text().equals(command());
    }
}
