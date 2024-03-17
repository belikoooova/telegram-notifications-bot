package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.entity.dto.RemoveLinkRequest;
import edu.java.bot.service.chat.ChatService;
import edu.java.bot.service.client.ScrapperClient;
import edu.java.bot.service.validation.LinkValidator;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private static final String TITLE = "/untrack";
    private static final String DESCRIPTION = "stop tracking a link";
    private static final String GET_URL = "Enter the link you want to stop tracking.";
    private static final String OK = "The link was successfully deleted.";
    private static final String NOT_OK = "Sorry, your link is incorrect. Please choose any command to continue.";

    private final ChatService chatService;
    private final LinkValidator linkValidator;
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
        Long userId = update.message().from().id();
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        if (chatService.getChatState(userId).equals(ChatState.AWAITING_UNTRACK_URL)) {
            try {
                String url = linkValidator.getValidatedAndNormalizedUrl(text);
                scrapperClient.untrackLink(chatId, new RemoveLinkRequest(URI.create(url)));
                chatService.setChatState(chatId, ChatState.NONE);
                return new SendMessage(chatId, OK);
            } catch (IllegalArgumentException e) {
                chatService.setChatState(chatId, ChatState.NONE);
                return new SendMessage(chatId, NOT_OK);
            }
        }
        chatService.setChatState(userId, ChatState.AWAITING_UNTRACK_URL);
        return new SendMessage(chatId, GET_URL);
    }

    @Override
    public boolean supports(Update update) {
        Long chatId = update.message().chat().id();
        if (chatService.getChatState(chatId) == null) {
            return false;
        }
        if (chatService.getChatState(chatId).equals(ChatState.AWAITING_UNTRACK_URL)) {
            return true;
        }
        return update.message().text().equals(command());
    }
}
