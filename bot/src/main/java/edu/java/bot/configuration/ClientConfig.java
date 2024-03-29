package edu.java.bot.configuration;

import edu.java.bot.service.client.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final ApplicationConfig applicationConfig;

    @Bean
    public ScrapperClient scrapperClient(
        WebClient.Builder webClientBuilder
    ) {
        return new ScrapperClient(
            webClientBuilder,
            applicationConfig.baseUrl().scrapper(),

            applicationConfig.clientTimeout().minutes()
        );
    }
}
