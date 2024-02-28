package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.processor.UserMessageProcessor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Bot implements AutoCloseable, UpdatesListener {
    private final TelegramBot telegramBot;
    private final UserMessageProcessor messageProcessor;

    public Bot(ApplicationConfig applicationConfig, UserMessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
        this.telegramBot = new TelegramBot(applicationConfig.telegramToken());
    }

    @PostConstruct
    public void init() {
        start();
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update: updates) {
            if (update == null || update.message() == null) {
                continue;
            }
            telegramBot.execute(messageProcessor.process(update));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void start() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }
}
