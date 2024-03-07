package edu.java.bot.service.processor;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.Bot;
import edu.java.bot.entity.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LinkUpdateProcessor {
    private static final String MESSAGE = "An update occurred!\n\nLink:\n%s\n\nDescription:\n%s";
    private final Bot bot;

    public void process(LinkUpdateRequest request) {
        for (var chatId : request.getTgChatIds()) {
            bot.execute(new SendMessage(chatId, MESSAGE.formatted(request.getUrl(), request.getDescription())));
        }
    }
}
