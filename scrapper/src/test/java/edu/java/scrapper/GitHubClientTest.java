package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.service.client.GitHubClient;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class GitHubClientTest {
    private static final String LOGIN = "belikoooova";
    private static final String TITLE = "tinkoff-course";
    private static final int HTTP_OK = 200;
    private static final OffsetDateTime DATE_TIME = OffsetDateTime.parse("2021-02-20T14:30:00Z");

    private WireMockServer wireMockServer;
    private GitHubClient gitHubClient;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        WebClient.Builder webClientBuilder = WebClient.builder();
        gitHubClient = new GitHubClient(webClientBuilder, wireMockServer.baseUrl());
    }

    @Test
    void fetchRepositoryShouldReturnRepositoryDetails() {
        String jsonResponse = """
            {
              "owner": {"login": "belikoooova"},
              "name": "tinkoff-course",
              "created_at": "2021-02-20T14:30:00Z",
              "updated_at": "2021-02-21T14:30:00Z",
              "pushed_at": "2021-02-22T14:30:00Z"
            }""";

        wireMockServer.stubFor(get(WireMock.urlEqualTo("/repos/belikoooova/tinkoff-course"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));

        StepVerifier
            .create(gitHubClient.fetchRepository(LOGIN, TITLE))
            .expectNextMatches(repositoryResponse ->
                repositoryResponse.owner().login().equals(LOGIN)
                    && repositoryResponse.title().equals(TITLE)
                    && repositoryResponse.createdTime().equals(DATE_TIME))
            .verifyComplete();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
