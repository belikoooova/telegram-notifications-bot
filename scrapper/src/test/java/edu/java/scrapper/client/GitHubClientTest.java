package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.entity.dto.RepositoryResponse;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.client.GitHubClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GitHubClientTest {
    private static final String LOGIN = "belikoooova";
    private static final int TIMEOUT = 5;
    private static final String TITLE = "tinkoff-course";
    private static final int HTTP_OK = 200;
    private static final OffsetDateTime DATE_TIME = OffsetDateTime.parse("2021-02-20T14:30:00Z");
    private static final String CORRECT_URL_GH_1 = "https://github.com/belikoooova/map-kit-app";
    private static final String CORRECT_URL_GH_2 = "https://github.com/belikoooova/map-kit-app/";
    private static final String INCORRECT_URL_GH_1 = "https://github.com/belikoooova/";
    private static final String INCORRECT_URL_GH_2 = "https://github.com/belikoooova/non-existing-repo/sed";
    private static final String INCORRECT_URL_GH_3 = "https://notgithub.com/belikoooova/hello";

    private WireMockServer wireMockServer;
    private GitHubClient gitHubClient;
    private final LinkService linkService = mock(LinkService.class);

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        WebClient.Builder webClientBuilder = WebClient.builder();
        gitHubClient = new GitHubClient(linkService, webClientBuilder, wireMockServer.baseUrl(), TIMEOUT);
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

    @ParameterizedTest
    @ValueSource(strings = {CORRECT_URL_GH_1, CORRECT_URL_GH_2})
    void testCanHandleCorrectLink(String link) {
        assertTrue(gitHubClient.canHandle(link));
    }

    @ParameterizedTest
    @ValueSource(strings = {INCORRECT_URL_GH_1, INCORRECT_URL_GH_2, INCORRECT_URL_GH_3})
    void testCanHandleIncorrectLink(String link) {
        assertFalse(gitHubClient.canHandle(link));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
