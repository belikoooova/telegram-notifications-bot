package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.repository.chat.ChatRepository;
import edu.java.bot.service.chat.ChatService;
import edu.java.bot.service.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String TITLE = "/start";
    private static final String DESCRIPTION = "register user";
    private static final String ANSWER = "Hello! This bot will help you track updates on the resources you need "
        + "(currently supports StackOverflow and GitHub). Type /help to see the list of commands.";
    // private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final ScrapperClient scrapperClient;

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
        Long chatId = update.message().from().id();
        scrapperClient.registerChat(chatId);
        chatService.addEmptyChatWithId(chatId);
        return new SendMessage(chatId, ANSWER);
    }
}
