package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.link.Link;
import edu.java.bot.service.link.factory.LinkFactory;
import edu.java.bot.service.link.repository.LinkRepository;
import edu.java.bot.service.user.state.UserState;
import edu.java.bot.service.user.state.UserStateService;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    private boolean isAwaiting = false;
    private LinkRepository linkRepository;
    @Autowired private UserStateService userStateService;
    @Autowired private LinkFactory linkFactory;

    @Autowired
    public TrackCommand(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public String command() {
        return CommandTitle.TRACK.toString();
    }

    @Override
    public String description() {
        return CommandDescription.TRACK.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        Long userId = update.message().from().id();
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        if (isAwaiting && userStateService.getUserState(userId).equals(UserState.AWAITING_URL)) {
            try {
                Link link = linkFactory.create(userId, text);
                linkRepository.save(link);
                isAwaiting = false;
                userStateService.clearUserState(userId);
                return new SendMessage(chatId, CommandHumanReadableMessage.TRACK_OK.toString());
            } catch (URISyntaxException | IllegalArgumentException e) {
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
