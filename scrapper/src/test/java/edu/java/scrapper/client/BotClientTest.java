package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import edu.java.scrapper.service.client.BotClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BotClientTest {
    private WireMockServer wireMockServer;
    private BotClient botClient;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        WebClient.Builder webClientBuilder = WebClient.builder();
        botClient = new BotClient(webClientBuilder, wireMockServer.baseUrl());
    }

    @Test
    void testSendUpdateSuccess() {
        LinkUpdateRequest request = new LinkUpdateRequest(1L, null, "Description", null);

        wireMockServer.stubFor(post(WireMock.urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withBody("Update processed")
                .withStatus(200)));

        String response = botClient.sendUpdate(request);

        assertEquals("Update processed", response);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
