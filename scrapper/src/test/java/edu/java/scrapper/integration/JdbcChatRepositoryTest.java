package edu.java.scrapper.integration;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcChatRepositoryTest extends IntegrationEnvironment {
    private static final long EXAMPLE_ID_1 = 1;
    private static final long EXAMPLE_ID_2 = 2;
    private static final int ADDED_CHATS_AMOUNT = 2;

    @Autowired
    private JdbcChatRepository chatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        Chat chat = new Chat(EXAMPLE_ID_1);

        Chat inserted = chatRepository.save(chat);

        assertEquals(chat.getId(), inserted.getId());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Chat chat1 = new Chat(EXAMPLE_ID_1);
        Chat chat2 = new Chat(EXAMPLE_ID_2);
        chatRepository.save(chat1);
        chatRepository.save(chat2);

        List<Chat> receivedChats = chatRepository.findAll();

        assertEquals(ADDED_CHATS_AMOUNT, receivedChats.size());
    }

    @Test
    @Transactional
    @Rollback
    void removeExistingTest() {
        Chat chat = new Chat(EXAMPLE_ID_1);
        Chat inserted = chatRepository.save(chat);

        Chat removed = chatRepository.remove(inserted);

        assertTrue(chatRepository.findAll().isEmpty());
        assertEquals(inserted.getId(), removed.getId());
    }

    @Test
    @Transactional
    @Rollback
    void removeNonExistingTest() {
        Chat chat = new Chat(EXAMPLE_ID_1);
        Chat inserted = chatRepository.save(chat);
        chatRepository.remove(inserted);

        assertThrows(
            org.springframework.dao.EmptyResultDataAccessException.class,
            () -> chatRepository.remove(inserted)
        );
    }
}
