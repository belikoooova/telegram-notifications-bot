package edu.java.scrapper.service.client;

import edu.java.scrapper.entity.dto.RepositoryResponse;
import java.time.Duration;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient {
    private final int timeoutInMinutes;
    private final WebClient webClient;

    public GitHubClient(
        WebClient.Builder webClientBuilder,
        String baseUrl,
        int timeoutInMinutes
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.timeoutInMinutes = timeoutInMinutes;
    }

    public RepositoryResponse fetchRepository(String owner, String name) {
        return webClient.get()
            .uri("/repos/{owner}/{name}", owner, name)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block(Duration.ofSeconds(timeoutInMinutes));
    }
}
