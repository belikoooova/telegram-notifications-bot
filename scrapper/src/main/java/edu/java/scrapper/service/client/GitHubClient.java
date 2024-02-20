package edu.java.scrapper.service.client;

import edu.java.scrapper.service.client.model.RepositoryResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    public GitHubClient(WebClient.Builder webClientBuilder, String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<RepositoryResponse> fetchRepository(String owner, String name) {
        return webClient.get()
            .uri("/repos/{owner}/{name}", owner, name)
            .retrieve()
            .bodyToMono(RepositoryResponse.class);
    }
}
