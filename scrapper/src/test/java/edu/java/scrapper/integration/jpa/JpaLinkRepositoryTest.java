package edu.java.scrapper.integration.jpa;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.integration.IntegrationEnvironment;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JpaLinkRepositoryTest extends IntegrationEnvironment {
    private static final URI EXAMPLE_URI_1 = URI.create("http://example4.com");
    private static final URI EXAMPLE_URI_2 = URI.create("http://example5.com");
    private static final OffsetDateTime DATE = OffsetDateTime.now();
    private static final int ADDED_LINKS_AMOUNT = 2;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Test
    @Rollback
    @Transactional
    @DirtiesContext
    void addTest() {
        Link link = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();

        Link inserted = linkRepository.save(link);

        assertNotNull(inserted.getId());
        assertEquals(link.getUrl(), inserted.getUrl());
        assertEquals(toZonedDateTime(link.getLastCheckedAt()), toZonedDateTime(inserted.getLastCheckedAt()));
    }

    @Test
    @Rollback
    @Transactional
    @DirtiesContext
    void findAllTest() {
        Link link1 = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link link2 = Link.builder()
            .url(EXAMPLE_URI_2)
            .lastCheckedAt(DATE)
            .build();
        linkRepository.save(link1);
        linkRepository.save(link2);

        List<Link> receivedLinks = linkRepository.findAll();

        assertEquals(ADDED_LINKS_AMOUNT, receivedLinks.size());
    }

    @Test
    @Rollback
    @Transactional
    @DirtiesContext
    void removeExistingTest() {
        Link link = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link inserted = linkRepository.save(link);

        linkRepository.delete(inserted);

        assertTrue(linkRepository.findAll().isEmpty());
    }

    @Test
    @Rollback
    @Transactional
    @DirtiesContext
    void removeNonExistingTest() {
        Link link = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link inserted = linkRepository.save(link);
        linkRepository.delete(inserted);

        assertDoesNotThrow(
            () -> linkRepository.delete(inserted)
        );
    }

    @Test
    @Rollback
    @DirtiesContext
    void testUpdateLastCheckedTime() {
        Link link1 = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link insertedLink1 = linkRepository.save(link1);
        OffsetDateTime newDate = DATE.plusHours(1);

        linkRepository.updateLastCheckedTime(insertedLink1.getId(), newDate);

        assertEquals(
            toZonedDateTime(newDate),
            toZonedDateTime(linkRepository.findByUrl(insertedLink1.getUrl()).get()
                .getLastCheckedAt())
        );
    }

    private ZonedDateTime toZonedDateTime(OffsetDateTime dateTime) {
        return ZonedDateTime.parse(dateTime.toString())
            .withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS);
    }
}
