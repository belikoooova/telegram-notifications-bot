package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.user.state.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    private static final String TITLE = "/start";
    private static final String DESCRIPTION = "register user";
    private static final String ANSWER = "Hello! This bot will help you track updates on the resources you need "
        + "(currently supports StackOverflow and GitHub). Type /help to see the list of commands.";
    private final UserRepository userRepository;

    @Autowired
    public StartCommand(UserRepository userRepository) {
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
        return new SendMessage(update.message().chat().id(), ANSWER);
    }
}
