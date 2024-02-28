package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.link.Link;
import java.net.URISyntaxException;
import edu.java.bot.entity.user.UserState;
import edu.java.bot.repository.link.LinkRepository;
import edu.java.bot.repository.user.UserRepository;
import edu.java.bot.service.factory.LinkFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final String TITLE = "/track";
    private static final String DESCRIPTION = "start tracking a link";
    private static final String GET_URL = "Enter the link you want to start tracking.";
    private static final String OK = "The link was successfully added.";
    private static final String NOT_OK = "Sorry, your link is incorrect. Please choose any command to continue.";

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final LinkFactory linkFactory;

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
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        if (userRepository.getUserState(userId).equals(UserState.AWAITING_TRACK_URL)) {
            try {
                Link link = linkFactory.create(text);
                linkRepository.addLink(link);
                userRepository.getUserLinks(userId).add(link); // Temporary stub
                userRepository.clearUserState(userId);
                return new SendMessage(chatId, OK);
            } catch (IllegalArgumentException e) {
                userRepository.clearUserState(userId);
                return new SendMessage(chatId, NOT_OK);
            }
        }
        userRepository.setUserState(userId, UserState.AWAITING_TRACK_URL);
        return new SendMessage(chatId, GET_URL);
    }

    @Override
    public boolean supports(Update update) {
        Long userId = update.message().from().id();
        if (userRepository.getUserState(userId) == null) {
            return false;
        }
        if (userRepository.getUserState(userId).equals(UserState.AWAITING_TRACK_URL)) {
            return true;
        }
        return update.message().text().equals(command());
    }
}
