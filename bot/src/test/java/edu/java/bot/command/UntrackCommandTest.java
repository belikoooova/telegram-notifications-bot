package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.service.chat.ChatService;
import edu.java.bot.service.client.ScrapperClient;
import edu.java.bot.service.command.UntrackCommand;
import edu.java.bot.service.validation.LinkValidator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UntrackCommandTest {
    private static final long CHAT_ID = 123L;

    @Mock
    private ChatService chatService = mock(ChatService.class);
    @Mock
    private ScrapperClient scrapperClient = mock(ScrapperClient.class);
    @Mock
    private LinkValidator linkValidator = mock(LinkValidator.class);

    @InjectMocks
    private UntrackCommand untrackCommand;

    @Test
    void testUntrackCommandAwaitingUrl() {
        Update mockUpdate = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(mockUpdate.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(message.text()).thenReturn("/untrack");
        when(chatService.getChatState(CHAT_ID)).thenReturn(ChatState.NONE);
        untrackCommand = new UntrackCommand(chatService, linkValidator, scrapperClient);

        SendMessage response = untrackCommand.handle(mockUpdate);

        verify(chatService).setChatState(CHAT_ID, ChatState.AWAITING_UNTRACK_URL);
        assertEquals("Enter the link you want to stop tracking.", response.getParameters().get("text"));
        assertEquals(CHAT_ID, response.getParameters().get("chat_id"));
    }

    @Test
    void testSupportsShouldReturnFalseWhenStateIsNoneAndCommandIsNotTrack() {
        Update mockUpdate = mock(Update.class);
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(mockUpdate.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockChat.id()).thenReturn(CHAT_ID);
        when(mockMessage.text()).thenReturn("/something");
        when(chatService.getChatState(CHAT_ID)).thenReturn(ChatState.NONE);

        untrackCommand = new UntrackCommand(chatService, linkValidator, null);

        assertFalse(untrackCommand.supports(mockUpdate));
    }

    @Test
    void testSupportsShouldReturnTrueForCommandTrackRegardlessOfState() {
        Update mockUpdate = mock(Update.class);
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(mockUpdate.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockChat.id()).thenReturn(CHAT_ID);
        when(mockMessage.text()).thenReturn("/untrack");
        when(chatService.getChatState(CHAT_ID)).thenReturn(ChatState.NONE);

        untrackCommand = new UntrackCommand(chatService, linkValidator, null);

        assertTrue(untrackCommand.supports(mockUpdate));
    }

    @Test
    void testSupportsShouldReturnTrueWhenAwaitingUrl() {
        Update mockUpdate = mock(Update.class);
        Message mockMessage = mock(Message.class);
        Chat mockChat = mock(Chat.class);
        when(mockUpdate.message()).thenReturn(mockMessage);
        when(mockMessage.chat()).thenReturn(mockChat);
        when(mockChat.id()).thenReturn(CHAT_ID);
        when(mockMessage.text()).thenReturn("/something");
        when(chatService.getChatState(CHAT_ID)).thenReturn(ChatState.AWAITING_UNTRACK_URL);

        untrackCommand = new UntrackCommand(chatService, linkValidator, null);

        assertTrue(untrackCommand.supports(mockUpdate));
    }
}
