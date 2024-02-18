package edu.java.bot;

import edu.java.bot.service.link.Link;
import edu.java.bot.service.link.LinkResource;
import edu.java.bot.service.link.factory.SimpleLinkFactory;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class SimpleLinkFactoryTest {
    private static final String CORRECT_URL_GH_1 = "https://github.com/belikoooova/telegram-notifications-bot";
    private static final String CORRECT_URL_GH_2 = "https://github.com/belikoooova/telegram-notifications-bot/";
    private static final String INCORRECT_URL_GH_1 = "https://github.com/belikoooova/";
    private static final String INCORRECT_URL_GH_2 = "https://github.com/belikoooova/non-existing-repo";
    private static final String CORRECT_URL_SO_1 =
        "https://stackoverflow.com/questions/454396/forum-for-asp-net-mvc-site/454408#454408";
    private static final String CORRECT_URL_SO_2 =
        "https://stackoverflow.com/questions/454396";
    private static final String INCORRECT_URL_SO_1 = "https://stackoverflow.com/questionsS/4";
    private static final String INCORRECT_URL_SO_2 = "https://stackoverflow.com/questionsS/4";
    private static final long USER_ID = 1;

    private SimpleLinkFactory linkFactory;

    @BeforeEach
    void setUp() {
        linkFactory = new SimpleLinkFactory();
    }

    @ParameterizedTest
    @ValueSource(strings = {CORRECT_URL_GH_1, CORRECT_URL_GH_2})
    @DisplayName("Create link with valid GitHub URL")
    void testCreateLinkWithValidURLGH(String url) {
        try {
            Link link = linkFactory.create(USER_ID, url);
            assertNotNull(link);
            assertEquals(url, link.getUrl().toString());
            assertEquals(LinkResource.GITHUB, link.getResource());
            assertEquals("belikoooova/telegram-notifications-bot", link.getIdOnResource());
        } catch (URISyntaxException e) {
            fail("URISyntaxException should not be thrown for a valid URL");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {INCORRECT_URL_GH_1, INCORRECT_URL_GH_2})
    @DisplayName("Create link with invalid GitHub URL")
    void testCreateLinkWithInvalidURLGH(String url) {
        try {
            Link link = linkFactory.create(USER_ID, url);
            assertNull(link);
        } catch (IllegalArgumentException | URISyntaxException e) {
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {CORRECT_URL_SO_1, CORRECT_URL_SO_2})
    @DisplayName("Create link with valid StackOverflow URL")
    void testCreateLinkWithValidURLSO(String url) {
        try {
            Link link = linkFactory.create(USER_ID, url);
            assertNotNull(link);
            assertEquals(url, link.getUrl().toString());
            assertEquals(LinkResource.STACKOVERFLOW, link.getResource());
            assertEquals("454396", link.getIdOnResource());
        } catch (URISyntaxException e) {
            fail("URISyntaxException should not be thrown for a valid URL");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {INCORRECT_URL_SO_1, INCORRECT_URL_SO_2})
    @DisplayName("Create link with invalid StackOverflow URL")
    void testCreateLinkWithInvalidURLSO(String url) {
        try {
            Link link = linkFactory.create(USER_ID, url);
            assertNull(link);
        } catch (IllegalArgumentException | URISyntaxException e) {
        }
    }
}
