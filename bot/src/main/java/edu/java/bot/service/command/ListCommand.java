package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.entity.dto.LinkResponse;
import edu.java.bot.entity.dto.ListLinkResponse;
import edu.java.bot.service.chat.ChatService;
import edu.java.bot.service.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private static final String TITLE = "/list";
    private static final String DESCRIPTION = "show the list of tracked links";
    private static final String BEGIN_OF_THE_ANSWER = "Here are the links I am tracking:";
    private static final String THERE_ARE_NO_LINKS = "Sorry, the list of tracked links is empty.";
    private static final String COMMANDS_SEPARATOR = "\n- ";
    private final ChatService chatService;
    // private final ChatRepository chatRepository;
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
        Long chatId = update.message().chat().id();
        chatService.setChatState(chatId, ChatState.NONE);
        return new SendMessage(chatId, getAnswer(chatId));
    }

    private String getAnswer(Long chatId) {
        ListLinkResponse listLinkResponse = scrapperClient.getLinks(chatId);
        if (listLinkResponse.getSize() <= 0) {
            return THERE_ARE_NO_LINKS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(BEGIN_OF_THE_ANSWER);
        for (LinkResponse linkResponse : listLinkResponse.getLinks()) {
            sb.append(COMMANDS_SEPARATOR);
            sb.append(linkResponse.getUrl());
        }
        return sb.toString();
    }
}
