package edu.java.bot.service.processor;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.bot.Bot;
import edu.java.bot.entity.dto.LinkUpdateRequest;
import edu.java.bot.exception.NoSuchChatException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LinkUpdateProcessor {
    private static final String MESSAGE = "An update occurred!\n\nLink:\n%s\n\nDescription:\n%s";
    private final Bot bot;

    public void process(LinkUpdateRequest request) {
        for (var chatId : request.getTgChatIds()) {
            SendResponse response = bot.execute(
                new SendMessage(chatId, MESSAGE.formatted(request.getUrl(), request.getDescription()))
            );
            if (response.errorCode() == HttpStatus.BAD_REQUEST.value()) {
                throw new NoSuchChatException(chatId);
            }
        }
    }
}
