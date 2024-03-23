package edu.java.scrapper.integration.jpa;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JpaLinkRepositoryTest {
    private static final URI EXAMPLE_URI_1 = URI.create("http://example.com");
    private static final URI EXAMPLE_URI_2 = URI.create("http://example2.com");
    private static final OffsetDateTime DATE = OffsetDateTime.now();
    private static final int ADDED_LINKS_AMOUNT = 2;
    private static final long EXAMPLE_ID_1 = 1;
    private static final long EXAMPLE_ID_2 = 2;

    @Autowired
    private JpaLinkRepository linkRepository;
    @Autowired
    private JpaChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
    void testFindLinkByUrl() {
        Link link1 = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link insertedLink1 = linkRepository.save(link1);

        Optional<Link> optionalLink1 = linkRepository.findByUrl(EXAMPLE_URI_1);
        Optional<Link> optionalLink2 = linkRepository.findByUrl(EXAMPLE_URI_2);

        assertTrue(optionalLink1.isPresent());
        assertEquals(optionalLink1.get().getId(), insertedLink1.getId());
        assertTrue(optionalLink2.isEmpty());
    }

    @Test
    void testUpdateLastCheckedTime() throws InterruptedException {
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
