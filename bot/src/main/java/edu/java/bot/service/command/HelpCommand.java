package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.user.state.UserStateService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    @Autowired private List<Command> commands;
    @Autowired private UserStateService userStateService;

    @Override
    public String command() {
        return CommandTitle.HELP.toString();
    }

    @Override
    public String description() {
        return CommandDescription.HELP.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        userStateService.clearUserState(update.message().from().id());
        return new SendMessage(update.message().chat().id(), getAnswer());
    }

    private String getAnswer() {
        StringBuilder sb = new StringBuilder();
        sb.append(CommandHumanReadableMessage.HELP_BEGIN_OF_THE_ANSWER);
        for (int i = 0; i < commands.size(); ++i) {
            sb.append(commands.get(i).command());
            sb.append(CommandHumanReadableMessage.HELP_COMMANDS_SEPARATOR);
            sb.append(commands.get(i).description());
            if (i != commands.size() - 1) {
                sb.append(CommandHumanReadableMessage.HELP_LINES_SEPARATOR);
            }
        }
        return sb.toString();
    }
}
