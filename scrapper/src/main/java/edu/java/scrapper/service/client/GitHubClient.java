package edu.java.scrapper.service.client;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.LastCommitResponse;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import edu.java.scrapper.entity.dto.RepositoryResponse;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.service.LinkService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
    private static final String REPO_UPDATE_RESPONSE = "There have been some updates in the repository %s...";
    private static final String NEW_COMMITS_UPDATE_RESPONSE =
        "New commits were created in the repository %s (the last one is from %s)...";

    private final LinkService linkService;
    private final int timeoutInMinutes;
    private final WebClient webClient;

    public GitHubClient(
        LinkService linkService,
        WebClient.Builder webClientBuilder,
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
    public List<LinkUpdateRequest> handle(Link link) {
        Matcher matcher = REGEX.matcher(link.getUrl().toString());
        if (!matcher.find()) {
            throw new InvalidLinkException();
        }
        String owner = matcher.group(OWNER_GROUP);
        String repo = matcher.group(REPO_GROUP);
        List<Long> chatIds = linkService.listChatIdsByLinkId(link.getId());
        List<LinkUpdateRequest> linkUpdateRequests = new ArrayList<>();

        getRepositoryLinkUpdateRequest(link, owner, repo)
            .ifPresent(request -> {
                request.setTgChatIds(chatIds);
                linkUpdateRequests.add(request);
            });
        getLastCommitLinkUpdateRequest(link, owner, repo)
            .ifPresent(request -> {
                request.setTgChatIds(chatIds);
                linkUpdateRequests.add(request);
            });

        return linkUpdateRequests;
    }

    public Optional<LinkUpdateRequest> getRepositoryLinkUpdateRequest(Link link, String owner, String repo) {
        RepositoryResponse response = fetchRepository(owner, repo);
        if (response == null || response.updatedAt().isBefore(link.getLastCheckedAt())) {
            return Optional.empty();
        }
        return Optional.of(
            LinkUpdateRequest.builder()
                .id(link.getId())
                .url(link.getUrl())
                .description(REPO_UPDATE_RESPONSE.formatted(repo))
                .build()
        );
    }

    public Optional<LinkUpdateRequest> getLastCommitLinkUpdateRequest(Link link, String owner, String repo) {
        LastCommitResponse response = fetchLastCommit(owner, repo);
        if (response == null || response.commit().author().date().isBefore(link.getLastCheckedAt())) {
            return Optional.empty();
        }
        return Optional.of(
            LinkUpdateRequest.builder()
                .id(link.getId())
                .url(link.getUrl())
                .description(NEW_COMMITS_UPDATE_RESPONSE.formatted(repo, response.commit().author().name()))
                .build()
        );
    }

    public RepositoryResponse fetchRepository(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{name}", owner, repo)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block(Duration.ofMinutes(timeoutInMinutes));
    }

    public LastCommitResponse fetchLastCommit(String owner, String repo) {
        return webClient.get()
            .uri("/repos/{owner}/{name}/commits", owner, repo)
            .retrieve()
            .bodyToFlux(LastCommitResponse.class)
            .next()
            .block(Duration.ofMinutes(timeoutInMinutes));
    }
}
