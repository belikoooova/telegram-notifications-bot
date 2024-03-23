package edu.java.scrapper.configuration.database;

import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    private final JdbcChatRepository jdbcChatRepository;
    private final JdbcLinkRepository jdbcLinkRepository;

    @Bean
    public ChatService chatService() {
        return new JdbcChatService(jdbcChatRepository);
    }

    @Bean
    public LinkService linkService() {
        return new JdbcLinkService(jdbcLinkRepository);
    }
}
