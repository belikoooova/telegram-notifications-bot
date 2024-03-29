package edu.java.scrapper.integration;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
    private static final int ADDED_CHATS_AMOUNT = 2;
    private static final long EXAMPLE_ID_1 = 1;
    private static final long EXAMPLE_ID_2 = 2;

    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;
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
        Link inserted = linkRepository.save(link);
        linkRepository.remove(inserted);

        assertThrows(
            org.springframework.dao.EmptyResultDataAccessException.class,
            () -> linkRepository.remove(inserted)
        );
    }

    @Test
    @Transactional
    @Rollback
    void testConnectLinkToChat() {
        Link link1 = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link link2 = Link.builder()
            .url(EXAMPLE_URI_2)
            .lastCheckedAt(DATE)
            .build();
        Link insertedLink1 = linkRepository.save(link1);
        Link insertedLink2 = linkRepository.save(link2);
        Chat chat = new Chat(EXAMPLE_ID_1);
        chatRepository.save(chat);

        linkRepository.connectLinkToChat(EXAMPLE_ID_1, insertedLink1.getId());
        linkRepository.connectLinkToChat(EXAMPLE_ID_1, insertedLink2.getId());

        assertEquals(ADDED_LINKS_AMOUNT, linkRepository.findAllLinksByChatId(EXAMPLE_ID_1).size());
    }

    @Test
    @Transactional
    @Rollback
    void testFindAllLinksByChatId() {
        Link link1 = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link link2 = Link.builder()
            .url(EXAMPLE_URI_2)
            .lastCheckedAt(DATE)
            .build();
        Link insertedLink1 = linkRepository.save(link1);
        Link insertedLink2 = linkRepository.save(link2);
        Chat chat = new Chat(EXAMPLE_ID_1);
        chatRepository.save(chat);

        linkRepository.connectLinkToChat(EXAMPLE_ID_1, insertedLink1.getId());
        linkRepository.connectLinkToChat(EXAMPLE_ID_1, insertedLink2.getId());
        List<Link> addedLinks = linkRepository.findAllLinksByChatId(EXAMPLE_ID_1);

        assertEquals(ADDED_LINKS_AMOUNT, addedLinks.size());
    }

    @Test
    @Transactional
    @Rollback
    void testDisconnectLinkToChat() {
        Link link1 = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link link2 = Link.builder()
            .url(EXAMPLE_URI_2)
            .lastCheckedAt(DATE)
            .build();
        Link insertedLink1 = linkRepository.save(link1);
        Link insertedLink2 = linkRepository.save(link2);
        Chat chat = new Chat(EXAMPLE_ID_1);
        chatRepository.save(chat);
        linkRepository.connectLinkToChat(EXAMPLE_ID_1, insertedLink1.getId());
        linkRepository.connectLinkToChat(EXAMPLE_ID_1, insertedLink2.getId());

        linkRepository.disconnectLinkToChat(EXAMPLE_ID_1, insertedLink1.getId());

        assertEquals(1, linkRepository.findAllLinksByChatId(EXAMPLE_ID_1).size());
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

        Optional<Link> optionalLink1 = linkRepository.findLinkByUrl(EXAMPLE_URI_1);
        Optional<Link> optionalLink2 = linkRepository.findLinkByUrl(EXAMPLE_URI_2);

        assertTrue(optionalLink1.isPresent());
        assertEquals(optionalLink1.get().getId(), insertedLink1.getId());
        assertTrue(optionalLink2.isEmpty());
    }

    @Test
    @Transactional
    void testGetChatIdsByLinkId() {
        Link link1 = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link insertedLink1 = linkRepository.save(link1);
        Chat chat1 = new Chat(EXAMPLE_ID_1);
        chatRepository.save(chat1);
        Chat chat2 = new Chat(EXAMPLE_ID_2);
        chatRepository.save(chat2);
        linkRepository.connectLinkToChat(EXAMPLE_ID_1, insertedLink1.getId());
        linkRepository.connectLinkToChat(EXAMPLE_ID_2, insertedLink1.getId());

        List<Long> addedChatIds = linkRepository.getChatIdsByLinkId(insertedLink1.getId());

        assertEquals(ADDED_CHATS_AMOUNT, addedChatIds.size());
    }

    @Test
    @Transactional
    void testUpdateLastCheckedTime() {
        Link link1 = Link.builder()
            .url(EXAMPLE_URI_1)
            .lastCheckedAt(DATE)
            .build();
        Link insertedLink1 = linkRepository.save(link1);
        OffsetDateTime newDate = DATE.plusHours(1);

        linkRepository.updateLastCheckedTime(insertedLink1, newDate);

        assertEquals(
            toZonedDateTime(newDate),
            toZonedDateTime(linkRepository.findLinkByUrl(insertedLink1.getUrl()).get()
                .getLastCheckedAt())
        );
    }

    private ZonedDateTime toZonedDateTime(OffsetDateTime dateTime) {
        return ZonedDateTime.parse(dateTime.toString())
            .withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS);
    }
}