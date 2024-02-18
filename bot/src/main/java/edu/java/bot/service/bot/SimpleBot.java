package edu.java.bot.service.bot;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleBot implements Bot {
    private final ApplicationConfig applicationConfig;
    private final UserMessageProcessor messageProcessor;
    private TelegramBot bot;

    @Autowired
    public SimpleBot(ApplicationConfig applicationConfig, UserMessageProcessor messageProcessor) {
        this.applicationConfig = applicationConfig;
        this.messageProcessor = messageProcessor;
    }

    @PostConstruct
    public void init() {
        this.bot = new TelegramBot(applicationConfig.getTelegramToken());
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

    }
}
