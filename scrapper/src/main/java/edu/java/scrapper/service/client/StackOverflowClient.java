package edu.java.scrapper.service.client;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.LastAnswerResponse;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import edu.java.scrapper.entity.dto.QuestionResponse;
import edu.java.scrapper.exception.InvalidLinkException;
import edu.java.scrapper.service.LinkService;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient implements WebsiteClient {
    private static final Pattern REGEX =
        Pattern.compile(
            "^(.*\\.stackoverflow\\.com/|.*/stackoverflow\\.com/|stackoverflow\\.com/)(questions/\\d+)/?$",
            Pattern.CASE_INSENSITIVE
        );
    private static final int QUESTION_GROUP = 2;
    private static final String ACTIVITY_RESPONSE = "There was activity on the page with the question...";
    private static final String EDIT_RESPONSE = "The question has been edited...";
    private static final String NEW_ANSWER = "There is a new answer to the question...";

    private final LinkService linkService;
    private final int timeout;
    private final WebClient webClient;

    public StackOverflowClient(
        LinkService linkService, WebClient.Builder webClientBuilder,
        String baseUrl,
        int timeout
    ) {
        this.linkService = linkService;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.timeout = timeout;
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
        Long questionId = Long.getLong(matcher.group(matcher.group(QUESTION_GROUP)));
        List<Long> chatIds = linkService.listChatIdsByLinkId(link.getId());
        List<LinkUpdateRequest> linkUpdateRequests = new ArrayList<>();

        getQuestionLinkUpdateRequest(link, questionId)
            .ifPresent(request -> {
                request.setTgChatIds(chatIds);
                linkUpdateRequests.add(request);
            });
        getAnswerLinkUpdateRequest(link, questionId)
            .ifPresent(request -> {
                request.setTgChatIds(chatIds);
                linkUpdateRequests.add(request);
            });
        return linkUpdateRequests;
    }

    public Optional<LinkUpdateRequest> getQuestionLinkUpdateRequest(Link link, Long questionId) {
        QuestionResponse response = fetchQuestion(questionId);
        String answer;
        if (timeBeforeAnother(response.items().getFirst().lastEditDateSeconds(), link.getLastCheckedAt())) {
            answer = EDIT_RESPONSE;
        } else if (timeBeforeAnother(response.items().getFirst().lastActivityDateSeconds(), link.getLastCheckedAt())) {
            answer = ACTIVITY_RESPONSE;
        } else {
            return Optional.empty();
        }
        return Optional.of(
            LinkUpdateRequest.builder()
                .id(link.getId())
                .url(link.getUrl())
                .description(answer)
                .build()
        );
    }

    public QuestionResponse fetchQuestion(Long questionId) {
        return webClient.get()
            .uri("/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .block(Duration.ofMinutes(timeout));
    }

    public Optional<LinkUpdateRequest> getAnswerLinkUpdateRequest(Link link, Long questionId) {
        LastAnswerResponse response = fetchLastAnswer(questionId);
        if (response == null
            || response.items().isEmpty()
            || timeBeforeAnother(response.items().getFirst().creationDateSeconds(), link.getLastCheckedAt())
        ) {
            return Optional.empty();
        }
        return Optional.of(
            LinkUpdateRequest.builder()
                .id(link.getId())
                .url(link.getUrl())
                .description(NEW_ANSWER)
                .build()
        );
    }

    public LastAnswerResponse fetchLastAnswer(Long questionId) {
        return webClient.get()
            .uri("/questions/{questionId}/answers?order=desc&sort=activity&site=stackoverflow", questionId)
            .retrieve()
            .bodyToMono(LastAnswerResponse.class)
            .block(Duration.ofMinutes(timeout));
    }

    private boolean timeBeforeAnother(Long seconds, OffsetDateTime offsetDateTime) {
        return Instant.ofEpochSecond(seconds).atOffset(ZoneOffset.UTC)
            .isAfter(offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC));
    }
}
