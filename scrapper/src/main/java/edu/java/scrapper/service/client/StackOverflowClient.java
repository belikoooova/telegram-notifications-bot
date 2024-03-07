package edu.java.scrapper.service.client;

import edu.java.scrapper.entity.dto.QuestionResponse;
import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {
    private final int timeout;
    private final WebClient webClient;

    public StackOverflowClient(
        WebClient.Builder webClientBuilder,
        String baseUrl,
        int timeout
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.timeout = timeout;
    }

    public QuestionResponse fetchQuestion(Long questionId) {
        return webClient.get()
            .uri("/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .block(Duration.ofSeconds(timeout));
    }
}
