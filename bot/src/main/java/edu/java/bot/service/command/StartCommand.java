package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.user.state.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    @Override
    public String command() {
        return CommandTitle.START.toString();
    }

    @Autowired private UserStateService userStateService;

    @Override
    public String description() {
        return CommandDescription.START.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        userStateService.clearUserState(update.message().from().id());
        return new SendMessage(update.message().chat().id(), CommandHumanReadableMessage.START_ANSWER.toString());
    }
}
