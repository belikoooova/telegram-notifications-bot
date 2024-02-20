package edu.java.scrapper.service.client;

import edu.java.scrapper.service.client.model.QuestionResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackOverflowClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public StackOverflowClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<QuestionResponse> fetchQuestion(Long questionId) {
        return webClient.get()
            .uri("/2.3/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(QuestionResponse.class);
    }
}
