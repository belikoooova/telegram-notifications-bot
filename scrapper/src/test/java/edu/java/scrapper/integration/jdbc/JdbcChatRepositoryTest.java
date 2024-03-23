package edu.java.scrapper.integration.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.integration.IntegrationEnvironment;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcChatRepositoryTest extends IntegrationEnvironment {
    private static final long EXAMPLE_ID_1 = 11;
    private static final long EXAMPLE_ID_2 = 22;
    private static final int ADDED_CHATS_AMOUNT = 2;

    @Autowired
    private JdbcChatRepository chatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    void addTest() {
        Chat chat = Chat.builder().id(EXAMPLE_ID_1).build();

        Chat inserted = chatRepository.add(chat);

        assertEquals(chat.getId(), inserted.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    void findAllTest() {
        Chat chat1 = Chat.builder().id(EXAMPLE_ID_1).build();
        Chat chat2 = Chat.builder().id(EXAMPLE_ID_2).build();
        chatRepository.add(chat1);
        chatRepository.add(chat2);

        List<Chat> receivedChats = chatRepository.findAll();

        assertEquals(ADDED_CHATS_AMOUNT, receivedChats.size());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    void removeExistingTest() {
        Chat chat = Chat.builder().id(EXAMPLE_ID_1).build();
        Chat inserted = chatRepository.add(chat);

        Chat removed = chatRepository.remove(inserted);

        assertTrue(chatRepository.findAll().isEmpty());
        assertEquals(inserted.getId(), removed.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    void removeNonExistingTest() {
        Chat chat = Chat.builder().id(EXAMPLE_ID_1).build();
        Chat inserted = chatRepository.add(chat);
        chatRepository.remove(inserted);

        assertThrows(
            org.springframework.dao.EmptyResultDataAccessException.class,
            () -> chatRepository.remove(inserted)
        );
    }
}
