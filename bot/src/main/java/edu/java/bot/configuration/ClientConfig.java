package edu.java.bot.configuration;

import edu.java.bot.service.client.ScrapperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Bean
    public ScrapperClient scrapperClient(
        WebClient.Builder webClientBuilder,
        @Value("${scrapper.base.url:SCRAPPER_BASE_URL}") String scrapperBaseUrl
    ) {
        return new ScrapperClient(webClientBuilder, scrapperBaseUrl);
    }
}
