package edu.java.bot.service.client;

import edu.java.bot.entity.dto.AddLinkRequest;
import edu.java.bot.entity.dto.ApiErrorResponse;
import edu.java.bot.entity.dto.ListLinkResponse;
import edu.java.bot.entity.dto.RemoveLinkRequest;
import edu.java.bot.exception.ApiErrorResponseException;
import java.time.Duration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClient {
    private static final int TIMEOUT = 5;
    private static final String TG_CHAT = "/tg-chat/{chatId}";
    private static final String LINKS = "/links/{chatId}";
    private final WebClient webClient;

    public ScrapperClient(
        WebClient.Builder webClientBuilder,
        String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public String registerChat(Long chatId) {
        return webClient.post()
            .uri(TG_CHAT, chatId)
            .retrieve()
            .onStatus(
                status -> HttpStatus.BAD_REQUEST.equals(status) || HttpStatus.CONFLICT.equals(status),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofSeconds(TIMEOUT));
    }

    public String deleteChat(Long chatId) {
        return webClient.delete()
            .uri(TG_CHAT, chatId)
            .retrieve()
            .onStatus(
                status -> HttpStatus.BAD_REQUEST.equals(status) || HttpStatus.NOT_FOUND.equals(status),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofSeconds(TIMEOUT));
    }

    public ListLinkResponse getLinks(Long chatId) {
        return webClient.get()
            .uri(LINKS, chatId)
            .retrieve()
            .onStatus(
                status -> HttpStatus.BAD_REQUEST.equals(status) || HttpStatus.NOT_FOUND.equals(status),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(ListLinkResponse.class)
            .block(Duration.ofSeconds(TIMEOUT));
    }

    public String trackLink(Long chatId, AddLinkRequest request) {
        return webClient.post()
            .uri(LINKS, chatId)
            .bodyValue(request)
            .retrieve()
            .onStatus(
                status -> HttpStatus.BAD_REQUEST.equals(status)
                    || HttpStatus.NOT_FOUND.equals(status)
                    || HttpStatus.CONFLICT.equals(status),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofSeconds(TIMEOUT));
    }

    public String untrackLink(Long chatId, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS, chatId)
            .bodyValue(request)
            .retrieve()
            .onStatus(
                status -> HttpStatus.BAD_REQUEST.equals(status) || HttpStatus.NOT_FOUND.equals(status),
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            ).bodyToMono(String.class)
            .block(Duration.ofSeconds(TIMEOUT));
    }
}
