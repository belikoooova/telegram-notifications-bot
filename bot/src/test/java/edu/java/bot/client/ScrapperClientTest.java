package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.bot.entity.dto.AddLinkRequest;
import edu.java.bot.entity.dto.ListLinkResponse;
import edu.java.bot.entity.dto.RemoveLinkRequest;
import edu.java.bot.exception.ApiErrorResponseException;
import edu.java.bot.service.client.ScrapperClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScrapperClientTest {
    private static final Long EXISTING_CHAT_ID = 1L;
    private static final Long NOT_EXISTING_CHAT_ID = 2L;
    private static final int TIMEOUT_IN_MINUTES = 5;
    private WireMockServer wireMockServer;
    private ScrapperClient scrapperClient;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        WebClient.Builder webClientBuilder = WebClient.builder();
        scrapperClient = new ScrapperClient(webClientBuilder, wireMockServer.baseUrl(), TIMEOUT_IN_MINUTES);
    }

    @Test
    void testRegisterNewChat() {
        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/" + EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withBody("Чат успешно зарегистрирован")
                .withStatus(HttpStatus.OK.value())));

        String response = scrapperClient.registerChat(EXISTING_CHAT_ID);
        assertEquals("Чат успешно зарегистрирован", response);
    }

    @Test
    void testDeleteChat() {
        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/" + EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withBody("Чат успешно удалён")
                .withStatus(HttpStatus.OK.value())));

        String response = scrapperClient.deleteChat(EXISTING_CHAT_ID);
        assertEquals("Чат успешно удалён", response);
    }

    @Test
    void testDeleteNotExistingChat() {
        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/" + NOT_EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n" +
                    "  \"description\": \"Чат 2 не найден\",\n" +
                    "  \"code\": \"404\",\n" +
                    "  \"exceptionName\": \"NoSuchChatException\",\n" +
                    "  \"exceptionMessage\": null,\n" +
                    "  \"stacktrace\": []\n}")
                .withStatus(HttpStatus.NOT_FOUND.value())));

        ApiErrorResponseException exception = assertThrows(ApiErrorResponseException.class, () -> {
            scrapperClient.deleteChat(NOT_EXISTING_CHAT_ID);
        });

        assertEquals("Чат 2 не найден", exception.getApiErrorResponse().getDescription());
    }

    @Test
    void testAddAlreadyExistingChat() {
        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/" + EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n" +
                    "  \"description\": \"Чат 1 уже существует\",\n" +
                    "  \"code\": \"409\",\n" +
                    "  \"exceptionName\": \"ChatAlreadyExistsException\",\n" +
                    "  \"exceptionMessage\": null,\n" +
                    "  \"stacktrace\": []\n}")
                .withStatus(HttpStatus.CONFLICT.value())));

        ApiErrorResponseException exception = assertThrows(ApiErrorResponseException.class, () -> {
            scrapperClient.registerChat(EXISTING_CHAT_ID);
        });

        assertEquals("Чат 1 уже существует", exception.getApiErrorResponse().getDescription());
    }

    @Test
    void testGetLinks() {
        wireMockServer.stubFor(get(urlEqualTo("/links/" + EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"links\":[{\"url\":\"http://example.com\",\"description\":\"Example\"}]}")
                .withStatus(HttpStatus.OK.value())));

        ListLinkResponse response = scrapperClient.getLinks(EXISTING_CHAT_ID);
        assertEquals(1, response.getLinks().size());
        assertEquals("http://example.com", response.getLinks().get(0).getUrl().toString());
    }

    @Test
    void testTrackLink() {
        AddLinkRequest request = new AddLinkRequest(URI.create("http://example.com"));
        wireMockServer.stubFor(post(urlEqualTo("/links/" + EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withBody("Ссылка успешно добавлена")
                .withStatus(HttpStatus.OK.value())));

        String response = scrapperClient.trackLink(EXISTING_CHAT_ID, request);
        assertEquals("Ссылка успешно добавлена", response);
    }

    @Test
    void testTrackAlreadyTrackingLink() {
        AddLinkRequest request = new AddLinkRequest(URI.create("http://example.com"));
        wireMockServer.stubFor(post(urlEqualTo("/links/" + EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n" +
                    "  \"description\": \"Ссылка уже отслеживается\",\n" +
                    "  \"code\": \"409\",\n" +
                    "  \"exceptionName\": \"LinkAlreadyTracksException\",\n" +
                    "  \"exceptionMessage\": null,\n" +
                    "  \"stacktrace\": []\n}")
                .withStatus(HttpStatus.CONFLICT.value())));

        ApiErrorResponseException exception = assertThrows(ApiErrorResponseException.class, () -> {
            scrapperClient.trackLink(EXISTING_CHAT_ID, request);
        });

        assertEquals("Ссылка уже отслеживается", exception.getApiErrorResponse().getDescription());
    }

    @Test
    void testUntrackLink() {
        RemoveLinkRequest request = new RemoveLinkRequest(URI.create("http://example.com"));
        wireMockServer.stubFor(delete(urlEqualTo("/links/" + EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withBody("Ссылка успешно удалена")
                .withStatus(HttpStatus.OK.value())));

        String response = scrapperClient.untrackLink(EXISTING_CHAT_ID, request);
        assertEquals("Ссылка успешно удалена", response);
    }

    @Test
    void testUntrackNotExistingLink() {
        RemoveLinkRequest request = new RemoveLinkRequest(URI.create("http://example.com"));
        wireMockServer.stubFor(delete(urlEqualTo("/links/" + EXISTING_CHAT_ID))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n" +
                    "  \"description\": \"Ссылка не найдена\",\n" +
                    "  \"code\": \"404\",\n" +
                    "  \"exceptionName\": \"NoSuchLinkException\",\n" +
                    "  \"exceptionMessage\": null,\n" +
                    "  \"stacktrace\": []\n}")
                .withStatus(HttpStatus.NOT_FOUND.value())));

        ApiErrorResponseException exception = assertThrows(ApiErrorResponseException.class, () -> {
            scrapperClient.untrackLink(EXISTING_CHAT_ID, request);
        });

        assertEquals("Ссылка не найдена", exception.getApiErrorResponse().getDescription());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }
}
