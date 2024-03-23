package edu.java.bot.configuration.database;

import edu.java.bot.repository.jdbc.JdbcChatRepository;
import edu.java.bot.service.chat.ChatService;
import edu.java.bot.service.chat.jdbc.JdbcChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    private final JdbcChatRepository jdbcChatRepository;

    @Bean
    public ChatService chatService() {
        return new JdbcChatService(jdbcChatRepository);
    }
}
