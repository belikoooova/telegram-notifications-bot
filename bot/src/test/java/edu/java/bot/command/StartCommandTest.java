package edu.java.bot.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.chat.ChatService;
import edu.java.bot.service.client.ScrapperClient;
import edu.java.bot.service.command.StartCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StartCommandTest {
    private static final long CHAT_ID = 123L;
    private static final String ANSWER = "Hello! This bot will help you track updates on the resources you need "
        + "(currently supports StackOverflow and GitHub). Type /help to see the list of commands.";

    @Mock
    private ChatService chatService = mock(ChatService.class);
    @Mock
    private ScrapperClient scrapperClient = mock(ScrapperClient.class);
    @InjectMocks
    private StartCommand startCommand;

    private Update mockUpdate;

    @BeforeEach
    void setUp() {
        mockUpdate = mock(Update.class);
        Message message = mock(Message.class);
        User fromUser = mock(User.class);

        when(mockUpdate.message()).thenReturn(message);
        when(message.from()).thenReturn(fromUser);
        when(fromUser.id()).thenReturn(CHAT_ID);

        startCommand = new StartCommand(chatService, scrapperClient);
    }

    @Test
    void testStartCommandHandle() {
        SendMessage response = startCommand.handle(mockUpdate);

        verify(chatService).addEmptyChatWithId(CHAT_ID);
        verify(scrapperClient).registerChat(CHAT_ID);
        assertEquals(ANSWER, response.getParameters().get("text"));
        assertEquals(CHAT_ID, response.getParameters().get("chat_id"));
    }
}
