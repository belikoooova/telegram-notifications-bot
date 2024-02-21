package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.link.Link;
import edu.java.bot.repository.link.factory.LinkFactory;
import edu.java.bot.repository.link.repository.LinkRepository;
import edu.java.bot.repository.user.state.UserRepository;
import edu.java.bot.repository.user.state.UserState;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    private static final String TITLE = "/track";
    private static final String DESCRIPTION = "start tracking a link";
    private static final String GET_URL = "Enter the link you want to start tracking.";
    private static final String OK = "The link was successfully added.";
    private static final String NOT_OK = "Sorry, your link is incorrect. Please try again or choose another command.";
    private boolean isAwaiting = false;
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final LinkFactory linkFactory;

    @Autowired
    public TrackCommand(LinkRepository linkRepository, UserRepository userRepository, LinkFactory linkFactory) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.linkFactory = linkFactory;
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
        Long userId = update.message().from().id();
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        if (isAwaiting && userRepository.getUserState(userId).equals(UserState.AWAITING_URL)) {
            try {
                Link link = linkFactory.create(userId, text);
                linkRepository.save(link);
                isAwaiting = false;
                userRepository.clearUserState(userId);
                return new SendMessage(chatId, OK);
            } catch (URISyntaxException | IllegalArgumentException e) {
                return new SendMessage(chatId, NOT_OK);
            }
        }
        isAwaiting = true;
        userRepository.setUserState(userId, UserState.AWAITING_URL);
        return new SendMessage(chatId, GET_URL);
    }

    @Override
    public boolean supports(Update update) {
        Long userId = update.message().from().id();
        if (isAwaiting && userRepository.getUserState(userId).equals(UserState.AWAITING_URL)) {
            return true;
        }
        return update.message().text().equals(command());
    }
}
