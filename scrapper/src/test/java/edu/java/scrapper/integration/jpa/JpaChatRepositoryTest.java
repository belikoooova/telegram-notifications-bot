package edu.java.scrapper.integration.jpa;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.integration.IntegrationEnvironment;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JpaChatRepositoryTest extends IntegrationEnvironment {
    private static final long EXAMPLE_ID_1 = 1;
    private static final long EXAMPLE_ID_2 = 2;
    private static final int ADDED_CHATS_AMOUNT = 2;

    @Autowired
    private JpaChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    void addTest() {
        Chat chat = Chat.builder().id(EXAMPLE_ID_1).build();

        Chat inserted = chatRepository.save(chat);

        assertEquals(chat.getId(), inserted.getId());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    void findAllTest() {
        Chat chat1 = Chat.builder().id(EXAMPLE_ID_1).build();
        Chat chat2 = Chat.builder().id(EXAMPLE_ID_2).build();
        chatRepository.save(chat1);
        chatRepository.save(chat2);

        List<Chat> receivedChats = chatRepository.findAll();

        assertEquals(ADDED_CHATS_AMOUNT, receivedChats.size());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    void removeExistingTest() {
        Chat chat = Chat.builder().id(EXAMPLE_ID_1).build();
        Chat inserted = chatRepository.save(chat);

        chatRepository.delete(inserted);

        assertTrue(chatRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    @DirtiesContext
    void removeNonExistingTest() {
        Chat chat = Chat.builder().id(EXAMPLE_ID_1).build();
        Chat inserted = chatRepository.save(chat);
        chatRepository.delete(inserted);

        assertDoesNotThrow(
            () -> chatRepository.delete(inserted)
        );
    }
}
