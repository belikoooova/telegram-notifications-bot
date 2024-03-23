package edu.java.bot.configuration.database;

import edu.java.bot.repository.jpa.JpaChatRepository;
import edu.java.bot.service.chat.ChatService;
import edu.java.bot.service.chat.jpa.JpaChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfig {
    private final JpaChatRepository jpaChatRepository;

    @Bean
    public ChatService chatService() {
        return new JpaChatService(jpaChatRepository);
    }
}
