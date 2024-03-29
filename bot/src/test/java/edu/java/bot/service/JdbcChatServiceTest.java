package edu.java.bot.service;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.repository.jdbc.JdbcChatStateRepository;
import edu.java.bot.service.chat.jdbc.JdbcChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JdbcChatServiceTest {
    @Mock
    private JdbcChatStateRepository chatRepository;

    @InjectMocks
    private JdbcChatService chatService;

    private final Long CHAT_ID = 123L;
    private final ChatState CHAT_STATE = ChatState.NONE;

    @Test
    void testRegisterShouldAddNewChat() {
        Chat chat = new Chat();
        chat.setId(CHAT_ID);
        when(chatRepository.save(any(Chat.class))).thenReturn(chat);

        chatService.addEmptyChatWithId(CHAT_ID);

        verify(chatRepository).save(any(Chat.class));
    }

    @Test
    void setChatStateShouldCallRepositorySetChatStateMethod() {
        chatService.setChatState(CHAT_ID, CHAT_STATE);

        verify(chatRepository).setChatState(CHAT_ID, CHAT_STATE);
    }

    @Test
    void getChatStateShouldReturnChatStateFromRepository() {
        when(chatRepository.getChatState(CHAT_ID)).thenReturn(CHAT_STATE);

        ChatState returnedState = chatService.getChatState(CHAT_ID);

        verify(chatRepository).getChatState(CHAT_ID);
        assertEquals(CHAT_STATE, returnedState);
    }
}

