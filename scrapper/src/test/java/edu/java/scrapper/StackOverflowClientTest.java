package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.service.client.StackOverflowClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class StackOverflowClientTest {
    private static final Long QUESTION_ID = 78023169L;
    private static final int HTTP_OK = 200;

    private WireMockServer wireMockServer;
    private StackOverflowClient stackOverflowClient;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        WebClient.Builder webClientBuilder = WebClient.builder();
        stackOverflowClient = new StackOverflowClient(webClientBuilder, wireMockServer.baseUrl());
    }

    @Test
    void fetchQuestionShouldReturnQuestionDetails() {
        String jsonResponse = """
            {"items":[{"question_id":78023169,"creation_date":1708371892,"last_activity_date":1708372580,"last_edit_date":1708372580}],"has_more":false,"quota_max":300,"quota_remaining":274}
            """;

        wireMockServer.stubFor(get(WireMock.urlEqualTo(
            "/2.3/questions/" + QUESTION_ID + "?order=desc&sort=activity&site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonResponse)
                .withStatus(HTTP_OK)));

        StepVerifier
            .create(stackOverflowClient.fetchQuestion(QUESTION_ID))
            .expectNextMatches(questionResponse ->
                questionResponse.items().size() == 1 &&
                    questionResponse.items().get(0).questionId().equals(QUESTION_ID))
            .verifyComplete();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
