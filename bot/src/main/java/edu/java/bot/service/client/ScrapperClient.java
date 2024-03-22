package edu.java.bot.service.client;

import edu.java.bot.entity.dto.AddLinkRequest;
import edu.java.bot.entity.dto.ApiErrorResponse;
import edu.java.bot.entity.dto.ListLinkResponse;
import edu.java.bot.entity.dto.RemoveLinkRequest;
import edu.java.bot.exception.ApiErrorResponseException;
import java.time.Duration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClient {
    private static final String TG_CHAT = "/tg-chat/{chatId}";
    private static final String LINKS = "/links/{chatId}";
    private final WebClient webClient;
    private final int timeoutInMinutes;

    public ScrapperClient(
        WebClient.Builder webClientBuilder,
        String baseUrl,
        int timeoutInMinutes
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.timeoutInMinutes = timeoutInMinutes;
    }

    public String registerChat(Long chatId) {
        return webClient.post()
            .uri(TG_CHAT, chatId)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofSeconds(timeoutInMinutes));
    }

    public String deleteChat(Long chatId) {
        return webClient.delete()
            .uri(TG_CHAT, chatId)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofMinutes(timeoutInMinutes));
    }

    public ListLinkResponse getLinks(Long chatId) {
        return webClient.get()
            .uri(LINKS, chatId)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(ListLinkResponse.class)
            .block(Duration.ofMinutes(timeoutInMinutes));
    }

    public String trackLink(Long chatId, AddLinkRequest request) {
        return webClient.post()
            .uri(LINKS, chatId)
            .bodyValue(request)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofMinutes(timeoutInMinutes));
    }

    public String untrackLink(Long chatId, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS, chatId)
            .bodyValue(request)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofMinutes(timeoutInMinutes));
    }
}
