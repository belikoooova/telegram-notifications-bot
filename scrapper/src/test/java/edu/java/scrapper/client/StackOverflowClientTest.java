package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.LastAnswerResponse;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import edu.java.scrapper.entity.dto.QuestionResponse;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.client.StackOverflowClient;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class StackOverflowClientTest {
    private static final Long QUESTION_ID = 78023169L;
    private static final int TIMEOUT = 5;
    private static final int HTTP_OK = 200;

    private WireMockServer wireMockServer;
    private StackOverflowClient stackOverflowClient;
    private final LinkService linkService = mock(LinkService.class);
    private static final String CORRECT_URL_1 = "https://ru.stackoverflow.com/questions/965439";
    private static final String CORRECT_URL_2 = "https://ru.stackoverflow.com/questions/965439/";
    private static final String INCORRECT_URL_1 = "https://ru.stackoverflow.com/questions/";
    private static final String INCORRECT_URL_2 = "https://ru.stackoverflow.com/questions/965439/smth";
    private static final String INCORRECT_URL_3 = "https://ru.notstackoverflow.com/questions/965439";
    private static final Long ID = 1L;
    private static final URI EXAMPLE_URI = URI.create("https://example.com");
    private static final OffsetDateTime DATE = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        WebClient.Builder webClientBuilder = WebClient.builder();
        stackOverflowClient = new StackOverflowClient(linkService, webClientBuilder, wireMockServer.baseUrl(), TIMEOUT);
    }

    @Test
    void fetchQuestionShouldReturnQuestionDetails() {
        String jsonResponse = """
            {"items":[{"question_id":78023169,"creation_date":1708371892,"last_activity_date":1708372580,"last_edit_date":1708372580}],"has_more":false,"quota_max":300,"quota_remaining":274}
            """;
        Long creationDateSeconds = 1708371892L;
        Long lastActivityDateSeconds = 1708372580L;
        Long lastEditDateSeconds = 1708372580L;
        wireMockServer.stubFor(get(WireMock.urlEqualTo(
            "/questions/" + QUESTION_ID + "?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(QUESTION_ID);

        assertEquals(1, questionResponse.items().size());
        assertEquals(QUESTION_ID, questionResponse.items().get(0).questionId());
        assertEquals(creationDateSeconds, questionResponse.items().get(0).creationDateSeconds());
        assertEquals(lastActivityDateSeconds, questionResponse.items().get(0).lastActivityDateSeconds());
        assertEquals(lastEditDateSeconds, questionResponse.items().get(0).lastEditDateSeconds());
    }

    @Test
    void fetchLastAnswerShouldReturnLastAnswerDetails() {
        String jsonResponse = """
              {
              "items": [
                {
                  "creation_date": 1614556800
                },
                {
                  "creation_date": 1614643200
                }
              ]
            }
            """;
        Long lastCreationDateSeconds = 1614556800L;
        wireMockServer.stubFor(get(WireMock.urlEqualTo(
            "/questions/" + QUESTION_ID + "/answers?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));

        LastAnswerResponse response = stackOverflowClient.fetchLastAnswer(QUESTION_ID);
        assertEquals(2, response.items().size());
        assertEquals(lastCreationDateSeconds, response.items().getFirst().creationDateSeconds());
    }

    @Test
    void getQuestionLinkUpdateRequestShouldReturnOptionalEmptyWhenUpdatesNotNew() {
        String jsonResponse = """
            {"items":[{"question_id":78023169,"creation_date":1708371892,"last_activity_date":1708372580,"last_edit_date":1708372580}],"has_more":false,"quota_max":300,"quota_remaining":274}
            """;
        wireMockServer.stubFor(get(WireMock.urlEqualTo(
            "/questions/" + QUESTION_ID + "?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));
        Link link = Link.builder().lastCheckedAt(OffsetDateTime.now()).url(EXAMPLE_URI).id(ID).build();

        Optional<LinkUpdateRequest> request = stackOverflowClient.getQuestionLinkUpdateRequest(link, QUESTION_ID);

        assertTrue(request.isEmpty());
    }

    @Test
    void getQuestionLinkUpdateRequestShouldReturnOptionalPresentWithEditMessageWhenEditEarlierThanActivity() {
        String jsonResponse = """
            {"items":[{"question_id":78023169,"creation_date":1708371892,"last_activity_date":1708372580,"last_edit_date":1708372580}],"has_more":false,"quota_max":300,"quota_remaining":274}
            """;
        wireMockServer.stubFor(get(WireMock.urlEqualTo(
            "/questions/" + QUESTION_ID + "?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));
        Link link = Link.builder().lastCheckedAt(OffsetDateTime.MIN.plusYears(1)).url(EXAMPLE_URI).id(ID).build();

        Optional<LinkUpdateRequest> request = stackOverflowClient.getQuestionLinkUpdateRequest(link, QUESTION_ID);

        assertTrue(request.isPresent());
        assertEquals("The question has been edited...", request.get().getDescription());
    }

    @Test
    void getQuestionLinkUpdateRequestShouldReturnOptionalPresentWithActivityMessageWhenActivityEarlierThanEdit() {
        String jsonResponse = """
            {"items":[{"question_id":78023169,"creation_date":1708371892,"last_activity_date":1708372580,"last_edit_date":0}],"has_more":false,"quota_max":300,"quota_remaining":274}
            """;
        wireMockServer.stubFor(get(WireMock.urlEqualTo(
            "/questions/" + QUESTION_ID + "?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));
        Link link = Link.builder().lastCheckedAt(DATE).url(EXAMPLE_URI).id(ID).build();

        Optional<LinkUpdateRequest> request = stackOverflowClient.getQuestionLinkUpdateRequest(link, QUESTION_ID);

        assertTrue(request.isPresent());
        assertEquals("There was activity on the page with the question...", request.get().getDescription());
    }

    @Test
    void getAnswerLinkUpdateRequestShouldReturnOptionalPresent() {
        String jsonResponse = """
              {
              "items": [
                {
                  "creation_date": 1614556800
                },
                {
                  "creation_date": 1614643200
                }
              ]
            }
            """;
        wireMockServer.stubFor(get(WireMock.urlEqualTo(
            "/questions/" + QUESTION_ID + "/answers?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));
        Link link = Link.builder().lastCheckedAt(DATE).url(EXAMPLE_URI).id(ID).build();

        Optional<LinkUpdateRequest> request = stackOverflowClient.getAnswerLinkUpdateRequest(link, QUESTION_ID);

        assertTrue(request.isPresent());
        assertEquals("There is a new answer to the question...", request.get().getDescription());
    }

    @ParameterizedTest
    @ValueSource(strings = {CORRECT_URL_1, CORRECT_URL_2})
    void testCanHandleCorrectLink(String link) {
        assertTrue(stackOverflowClient.canHandle(link));
    }

    @ParameterizedTest
    @ValueSource(strings = {INCORRECT_URL_1, INCORRECT_URL_2, INCORRECT_URL_3})
    void testCanHandleIncorrectLink(String link) {
        assertFalse(stackOverflowClient.canHandle(link));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
