package edu.java.bot.integration;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.repository.jdbc.JdbcChatRepository;
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

        Chat inserted = chatRepository.add(chat);

        assertEquals(chat.getId(), inserted.getId());
        assertEquals(ChatState.NONE, inserted.getState());
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        Chat chat1 = new Chat(EXAMPLE_ID_1);
        Chat chat2 = new Chat(EXAMPLE_ID_2);
        chatRepository.add(chat1);
        chatRepository.add(chat2);

        List<Chat> receivedChats = chatRepository.findAll();

        assertEquals(ADDED_CHATS_AMOUNT, receivedChats.size());
    }

    @Test
    @Transactional
    @Rollback
    void removeExistingTest() {
        Chat chat = new Chat(EXAMPLE_ID_1);
        Chat inserted = chatRepository.add(chat);

        Chat removed = chatRepository.remove(inserted);

        assertTrue(chatRepository.findAll().isEmpty());
        assertEquals(inserted.getId(), removed.getId());
    }

    @Test
    @Transactional
    @Rollback
    void removeNonExistingTest() {
        Chat chat = new Chat(EXAMPLE_ID_1);
        Chat inserted = chatRepository.add(chat);
        chatRepository.remove(inserted);

        assertThrows(
            org.springframework.dao.EmptyResultDataAccessException.class,
            () -> chatRepository.remove(inserted)
        );
    }

    @Test
    @Transactional
    @Rollback
    void testGetChatState() {
        Chat chat = new Chat(EXAMPLE_ID_1);
        Chat inserted = chatRepository.add(chat);

        ChatState result = chatRepository.getChatState(inserted.getId());

        assertEquals(ChatState.NONE, result);
    }

    @Test
    @Transactional
    @Rollback
    void testSetChatState() {
        Chat chat = new Chat(EXAMPLE_ID_1);
        Chat inserted = chatRepository.add(chat);
        chatRepository.setChatState(chat.getId(), ChatState.AWAITING_TRACK_URL);

        ChatState result = chatRepository.getChatState(inserted.getId());

        assertEquals(ChatState.AWAITING_TRACK_URL, result);
    }
}
