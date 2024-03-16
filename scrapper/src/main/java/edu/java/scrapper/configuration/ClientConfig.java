package edu.java.scrapper.configuration;

import edu.java.scrapper.service.client.BotClient;
import edu.java.scrapper.service.client.GitHubClient;
import edu.java.scrapper.service.client.StackOverflowClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final ApplicationConfig applicationConfig;

    @Bean
    public GitHubClient gitHubClient(
        WebClient.Builder webClientBuilder
    ) {
        return new GitHubClient(
            webClientBuilder,
            applicationConfig.baseUrl().gitHub(),
            applicationConfig.timeout().minutes()
        );
    }

    @Bean
    public StackOverflowClient stackOverflowClient(
        WebClient.Builder webClientBuilder
    ) {
        return new StackOverflowClient(
            webClientBuilder,
            applicationConfig.baseUrl().stackOverflow(),
            applicationConfig.timeout().minutes()
        );
    }

    @Bean
    public BotClient botClient(
        WebClient.Builder webClientBuilder
    ) {
        return new BotClient(
            webClientBuilder,
            applicationConfig.baseUrl().bot(),
            applicationConfig.timeout().minutes()
        );
    }
}
