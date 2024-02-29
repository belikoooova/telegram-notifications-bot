package edu.java.bot.command;

import edu.java.bot.entity.link.Link;
import edu.java.bot.entity.link.TrackingResource;
import edu.java.bot.service.factory.LinkFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LinkFactoryTest {
    private static final String CORRECT_URL_GH_1 = "https://github.com/belikoooova/map-kit-app";
    private static final String CORRECT_URL_GH_2 = "https://github.com/belikoooova/map-kit-app/";
    private static final String INCORRECT_URL_GH_1 = "https://github.com/belikoooova/";
    private static final String INCORRECT_URL_GH_2 = "https://github.com/belikoooova/non-existing-repo";

    private LinkFactory linkFactory;

    @BeforeEach
    void setUp() {
        linkFactory = new LinkFactory();
    }

    @ParameterizedTest
    @ValueSource(strings = {CORRECT_URL_GH_1, CORRECT_URL_GH_2})
    @DisplayName("Create link with valid GitHub URL")
    void testCreateLinkWithValidURLGH(String url) {
        Link link = linkFactory.create(url);
        assertNotNull(link);
        assertEquals(CORRECT_URL_GH_1, link.getUrl().toString());
        assertEquals(TrackingResource.GITHUB, link.getResource());
    }

    @ParameterizedTest
    @ValueSource(strings = {INCORRECT_URL_GH_1, INCORRECT_URL_GH_2})
    @DisplayName("Create link with invalid GitHub URL")
    void testCreateLinkWithInvalidURLGH(String url) {
        try {
            Link link = linkFactory.create(url);
            Assertions.assertNull(link);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
