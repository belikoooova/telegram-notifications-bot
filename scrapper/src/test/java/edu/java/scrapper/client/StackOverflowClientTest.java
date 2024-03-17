package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.client.StackOverflowClient;
import edu.java.scrapper.entity.dto.QuestionResponse;
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

        wireMockServer.stubFor(get(WireMock.urlEqualTo(
            "/questions/" + QUESTION_ID + "?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));

        QuestionResponse questionResponse = stackOverflowClient.fetchQuestion(QUESTION_ID);

        assertEquals(1, questionResponse.items().size());
        assertEquals(QUESTION_ID, questionResponse.items().get(0).questionId());
    }

    @ParameterizedTest
    @ValueSource(strings = { CORRECT_URL_1, CORRECT_URL_2 })
    void testCanHandleCorrectLink(String link) {
        assertTrue(stackOverflowClient.canHandle(link));
    }

    @ParameterizedTest
    @ValueSource(strings = { INCORRECT_URL_1, INCORRECT_URL_2, INCORRECT_URL_3 })
    void testCanHandleIncorrectLink(String link) {
        assertFalse(stackOverflowClient.canHandle(link));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
