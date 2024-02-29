package edu.java.scrapper.configuration;

import edu.java.scrapper.service.client.BotClient;
import edu.java.scrapper.service.client.GitHubClient;
import edu.java.scrapper.service.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient gitHubClient(
        WebClient.Builder webClientBuilder,
        @Value("${github.base.url:https://api.github.com}") String githubBaseUrl
    ) {
        return new GitHubClient(webClientBuilder, githubBaseUrl);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(
        WebClient.Builder webClientBuilder,
        @Value("${stackoverflow.base.url:https://api.stackexchange.com/2.3}") String stackoverflowBaseUrl
    ) {
        return new StackOverflowClient(webClientBuilder, stackoverflowBaseUrl);
    }

    @Bean
    public BotClient botClient(
        WebClient.Builder webClientBuilder,
        @Value("${bot.base.url:localhost:8080}") String botBaseUrl
    ) {
        return new BotClient(webClientBuilder, botBaseUrl);
    }
}
