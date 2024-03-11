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
        @Value("${github.base.url:GITHUB_BASE_URL}") String githubBaseUrl,
        @Value("${github.timeout.minutes}") int timeout
    ) {
        return new GitHubClient(webClientBuilder, githubBaseUrl, timeout);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(
        WebClient.Builder webClientBuilder,
        @Value("${stackoverflow.base.url:STACKOVERFLOW_BASE_URL}") String stackoverflowBaseUrl,
        @Value("${stackowerflow.timeout.minutes}") int timeout
    ) {
        return new StackOverflowClient(webClientBuilder, stackoverflowBaseUrl, timeout);
    }

    @Bean
    public BotClient botClient(
        WebClient.Builder webClientBuilder,
        @Value("${bot.base.url:BOT_BASE_URL}") String botBaseUrl,
        @Value("${bot.timeout.minutes}") int timeout
    ) {
        return new BotClient(webClientBuilder, botBaseUrl, timeout);
    }
}
