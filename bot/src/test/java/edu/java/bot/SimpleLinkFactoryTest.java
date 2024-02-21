package edu.java.bot;

import edu.java.bot.repository.link.Link;
import edu.java.bot.repository.link.LinkResource;
import edu.java.bot.repository.link.factory.SimpleLinkFactory;
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
}
