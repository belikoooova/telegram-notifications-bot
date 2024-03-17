package edu.java.scrapper.service.client;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import edu.java.scrapper.entity.dto.RepositoryResponse;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.service.LinkService;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClient implements WebsiteClient {
    private static final Pattern REGEX =
        Pattern.compile(
            "^(.*\\.github\\.com/|.*/github\\.com/|github\\.com/)([A-Z0-9_-]+)/([A-Z0-9_-]+)/?$",
            Pattern.CASE_INSENSITIVE
        );
    private static final int OWNER_GROUP = 2;
    private static final int REPO_GROUP = 3;
    private static final String UPDATE_RESPONSE = "There have been some updates in the repository...";

    private final LinkService linkService;
    private final int timeoutInMinutes;
    private final WebClient webClient;

    public GitHubClient(
        LinkService linkService, WebClient.Builder webClientBuilder,
        String baseUrl,
        int timeoutInMinutes
    ) {
        this.linkService = linkService;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.timeoutInMinutes = timeoutInMinutes;
    }

    @Override
    public boolean canHandle(String url) {
        return REGEX.matcher(url).find();
    }

    @Override
    public Optional<LinkUpdateRequest> handle(Link link) {
        Matcher matcher = REGEX.matcher(link.getUrl().toString());
        if (!matcher.find()) {
            throw new InvalidLinkException();
        }
        RepositoryResponse response = fetchRepository(matcher.group(OWNER_GROUP), matcher.group(REPO_GROUP));
        if (response.updatedAt().isAfter(link.getLastCheckedAt())) {
            return Optional.of(
                LinkUpdateRequest.builder()
                    .id(link.getId())
                    .url(link.getUrl())
                    .description(UPDATE_RESPONSE)
                    .tgChatIds(linkService.listChatIdsByLinkId(link.getId()))
                    .build()
            );
        }
        return Optional.empty();
    }

    public RepositoryResponse fetchRepository(String owner, String name) {
        return webClient.get()
            .uri("/repos/{owner}/{name}", owner, name)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block(Duration.ofSeconds(timeoutInMinutes));
    }
}
