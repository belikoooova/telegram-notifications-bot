package edu.java.scrapper.service.client;

import edu.java.scrapper.service.client.model.RepositoryResponse;
import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private static final int TIMEOUT = 5;
    private final WebClient webClient;

    public GitHubClient(
        WebClient.Builder webClientBuilder,
        String baseUrl
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public RepositoryResponse fetchRepository(String owner, String name) {
        return webClient.get()
            .uri("/repos/{owner}/{name}", owner, name)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block(Duration.ofSeconds(TIMEOUT));
    }
}
