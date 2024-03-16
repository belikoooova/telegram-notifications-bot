package edu.java.scrapper.integration;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcLinkRepositoryTest extends IntegrationEnvironment {
    private static final URI EXAMPLE_URI_1 = URI.create("http://example.com");
    private static final URI EXAMPLE_URI_2 = URI.create("http://example2.com");
    private static final OffsetDateTime DATE = OffsetDateTime.now();
    private static final int ADDED_LINKS_AMOUNT = 2;

    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Link link = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();

        Link inserted = linkRepository.add(link);

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
        linkRepository.add(link1);
        linkRepository.add(link2);

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
        Link inserted = linkRepository.add(link);

        Link removed = linkRepository.remove(inserted);

        assertTrue(linkRepository.findAll().isEmpty());
        assertEquals(inserted.getUrl(), removed.getUrl());
        assertEquals(toZonedDateTime(inserted.getLastCheckedAt()), toZonedDateTime(removed.getLastCheckedAt()));
    }

    @Test
    @Transactional
    @Rollback
    void removeNonExistingTest() {
        Link link = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link inserted = linkRepository.add(link);
        linkRepository.remove(inserted);

        assertThrows(
            org.springframework.dao.EmptyResultDataAccessException.class,
            () -> linkRepository.remove(inserted)
        );
    }

    private ZonedDateTime toZonedDateTime(OffsetDateTime dateTime) {
        return ZonedDateTime.parse(dateTime.toString()).withZoneSameInstant(ZoneOffset.UTC);
    }
}
