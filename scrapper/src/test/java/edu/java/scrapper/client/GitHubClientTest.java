package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.service.client.GitHubClient;
import edu.java.scrapper.entity.dto.RepositoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        RepositoryResponse response = gitHubClient.fetchRepository(LOGIN, TITLE);

        assertEquals(LOGIN, response.owner().login());
        assertEquals(TITLE, response.name());
        assertEquals(DATE_TIME, response.createdAt());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
