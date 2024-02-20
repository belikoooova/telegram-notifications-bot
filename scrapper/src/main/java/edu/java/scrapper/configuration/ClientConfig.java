package edu.java.scrapper.configuration;

import edu.java.scrapper.service.client.GitHubClient;
import edu.java.scrapper.service.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Bean
    @Autowired
    public GitHubClient gitHubClient(WebClient.Builder webClientBuilder) {
        return new GitHubClient(webClientBuilder);
    }

    @Bean
    @Autowired
    public StackOverflowClient stackOverflowClient(WebClient.Builder webClientBuilder) {
        return new StackOverflowClient(webClientBuilder);
    }
}
