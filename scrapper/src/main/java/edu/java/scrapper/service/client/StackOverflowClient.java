package edu.java.scrapper.service.client;

import edu.java.scrapper.service.client.model.QuestionResponse;
import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {
    private static final int TIMEOUT = 5;
    private final WebClient webClient;

    public StackOverflowClient(
        WebClient.Builder webClientBuilder,
        String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public QuestionResponse fetchQuestion(Long questionId) {
        return webClient.get()
            .uri("/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .block(Duration.ofSeconds(TIMEOUT));
    }
}
