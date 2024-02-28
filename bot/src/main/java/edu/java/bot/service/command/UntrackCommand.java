package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.link.Link;
import edu.java.bot.entity.user.UserState;
import edu.java.bot.repository.user.UserRepository;
import edu.java.bot.service.factory.LinkFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private static final String TITLE = "/untrack";
    private static final String DESCRIPTION = "stop tracking a link";
    private static final String GET_URL = "Enter the link you want to stop tracking.";
    private static final String OK = "The link was successfully deleted.";
    private static final String NOT_OK = "Sorry, your link is incorrect. Please choose any command to continue.";
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
        if (userRepository.getUserState(userId).equals(UserState.AWAITING_UNTRACK_URL)) {
            try {
                Link link = linkFactory.create(text);
                userRepository.getUserLinks(userId).remove(link);
                userRepository.clearUserState(userId);
                return new SendMessage(chatId, OK);
            } catch (IllegalArgumentException e) {
                userRepository.clearUserState(userId);
                return new SendMessage(chatId, NOT_OK);
            }
        }
        userRepository.setUserState(userId, UserState.AWAITING_UNTRACK_URL);
        return new SendMessage(chatId, GET_URL);
    }

    @Override
    public boolean supports(Update update) {
        Long userId = update.message().from().id();
        if (userRepository.getUserState(userId) == null) {
            return false;
        }
        if (userRepository.getUserState(userId).equals(UserState.AWAITING_UNTRACK_URL)) {
            return true;
        }
        return update.message().text().equals(command());
    }
}
