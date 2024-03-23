package edu.java.scrapper.service.jpa;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatAlreadyExistsException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaChatServiceTest {
    @Mock
    private JpaChatRepository chatRepository;

    @InjectMocks
    private JpaChatService jpaChatService;

    private final long TG_CHAT_ID = 1L;

    @Test
    void testRegisterShouldAddNewChat() {
        Chat chat = new Chat();
        chat.setId(TG_CHAT_ID);
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        jpaChatService.register(TG_CHAT_ID);

        verify(chatRepository).save(any(Chat.class));
    }

    @Test
    void testRegisterShouldThrowExceptionOnDuplicateKey() {
        when(chatRepository.existsById(any(Long.class))).thenReturn(true);

        assertThrows(ChatAlreadyExistsException.class, () -> jpaChatService.register(TG_CHAT_ID));
    }

    @Test
    void testUnregisterShouldRemoveChat() {
        Chat chat = new Chat();
        chat.setId(TG_CHAT_ID);
        when(chatRepository.existsById(any(Long.class))).thenReturn(true);

        jpaChatService.unregister(TG_CHAT_ID);

        verify(chatRepository).deleteById(any(Long.class));
    }

    @Test
    void testUnregisterShouldThrowExceptionIfChatDoesNotExist() {
        when(chatRepository.existsById(any(Long.class))).thenReturn(false);

        assertThrows(NoSuchChatException.class, () -> jpaChatService.unregister(TG_CHAT_ID));
    }
}
