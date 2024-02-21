package edu.java.bot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.processor.UserMessageProcessor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SimpleBot implements Bot {
    private final UserMessageProcessor messageProcessor;
    private final TelegramBot bot;

    public SimpleBot(ApplicationConfig applicationConfig, UserMessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
        this.bot = new TelegramBot(applicationConfig.getTelegramToken());
    }

    @PostConstruct
    public void init() {
        start();
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() == null) {
                continue;
            }
            bot.execute(messageProcessor.process(update));
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        bot.setUpdatesListener(this);
    }

    @PreDestroy
    @Override
    public void close() {
        bot.shutdown();
    }
}
