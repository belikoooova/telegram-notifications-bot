package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatAlreadyExistsException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcChatServiceTest {

    @Mock
    private JdbcChatRepository chatRepository;

    @InjectMocks
    private JdbcChatService jdbcChatService;

    private final long TG_CHAT_ID = 1L;

    @Test
    void testRegisterShouldAddNewChat() {
        Chat chat = new Chat();
        chat.setId(TG_CHAT_ID);
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        jdbcChatService.register(TG_CHAT_ID);

        verify(chatRepository).save(any(Chat.class));
    }

    @Test
    void testRegisterShouldThrowExceptionOnDuplicateKey() {
        doThrow(DuplicateKeyException.class).when(chatRepository).save(any(Chat.class));

        assertThrows(ChatAlreadyExistsException.class, () -> jdbcChatService.register(TG_CHAT_ID));
    }

    @Test
    void testUnregisterShouldRemoveChat() {
        Chat chat = new Chat();
        chat.setId(TG_CHAT_ID);
        when(chatRepository.remove(any(Chat.class))).thenReturn(chat);

        jdbcChatService.unregister(TG_CHAT_ID);

        verify(chatRepository).remove(any(Chat.class));
    }

    @Test
    void testUnregisterShouldThrowExceptionIfChatDoesNotExist() {
        doThrow(NoSuchChatException.class).when(chatRepository).remove(any(Chat.class));

        assertThrows(NoSuchChatException.class, () -> jdbcChatService.unregister(TG_CHAT_ID));
    }
}
