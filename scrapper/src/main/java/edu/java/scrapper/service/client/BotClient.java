package edu.java.scrapper.service.client;

import edu.java.scrapper.entity.dto.ApiErrorResponse;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import edu.java.scrapper.exception.ApiErrorResponseException;
import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClient {
    private final WebClient webClient;
    private final int timeoutInMinutes;

    public BotClient(
        WebClient.Builder webClientBuilder,
        String baseUrl,
        int timeoutInMinutes
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.timeoutInMinutes = timeoutInMinutes;
    }

    public String sendUpdate(LinkUpdateRequest request) {
        return webClient.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofMinutes(timeoutInMinutes));
    }
}
