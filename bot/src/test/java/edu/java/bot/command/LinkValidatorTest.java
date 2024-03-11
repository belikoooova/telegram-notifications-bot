package edu.java.bot.command;

import edu.java.bot.service.validation.LinkValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LinkValidatorTest {
    private static final String CORRECT_URL_GH_1 = "https://github.com/belikoooova/map-kit-app";
    private static final String CORRECT_URL_GH_2 = "https://github.com/belikoooova/map-kit-app/";
    private static final String INCORRECT_URL_GH_1 = "https://github.com/belikoooova/";
    private static final String INCORRECT_URL_GH_2 = "https://github.com/belikoooova/non-existing-repo";

    private LinkValidator linkValidator;

    @BeforeEach
    void setUp() {
        linkValidator = new LinkValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {CORRECT_URL_GH_1, CORRECT_URL_GH_2})
    @DisplayName("Create link with valid GitHub URL")
    void testCreateLinkWithValidURLGH(String url) {
        String validatedAndNormalizedUrl = linkValidator.getValidatedAndNormalizedUrl(url);

        assertNotNull(validatedAndNormalizedUrl);
        assertEquals(CORRECT_URL_GH_1, validatedAndNormalizedUrl);
    }

    @ParameterizedTest
    @ValueSource(strings = {INCORRECT_URL_GH_1, INCORRECT_URL_GH_2})
    @DisplayName("Create link with invalid GitHub URL")
    void testCreateLinkWithInvalidURLGH(String url) {
        try {
            String validatedAndNormalizedUrl = linkValidator.getValidatedAndNormalizedUrl(url);
            Assertions.assertNull(validatedAndNormalizedUrl);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
