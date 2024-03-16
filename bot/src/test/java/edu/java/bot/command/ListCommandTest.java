package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.entity.dto.LinkResponse;
import edu.java.bot.entity.dto.ListLinkResponse;
import edu.java.bot.repository.chat.ChatRepository;
import edu.java.bot.service.chat.ChatService;
import edu.java.bot.service.client.ScrapperClient;
import edu.java.bot.service.command.ListCommand;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListCommandTest {
    private static final long CHAT_ID = 123L;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ListCommand listCommand;
    private final Update update = mock(Update.class);
    private final Message message = mock(Message.class);
    private final Chat chat = mock(Chat.class);
    private final ScrapperClient scrapperClient = mock(ScrapperClient.class);

    @BeforeEach
    void setUp() {
        chatService = mock(ChatService.class);
        listCommand = new ListCommand(chatService, scrapperClient);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
    }

    @Test
    @DisplayName("Handle without links")
    void testHandleNoLinks() {
        when(scrapperClient.getLinks(CHAT_ID)).thenReturn(new ListLinkResponse(new ArrayList<>(), 0));

        SendMessage result = listCommand.handle(update);

        verify(chatService).setChatState(CHAT_ID, ChatState.NONE);
        Assertions.assertEquals(CHAT_ID, result.getParameters().get("chat_id"));
        Assertions.assertEquals(
            "Sorry, the list of tracked links is empty.",
            result.getParameters().get("text")
        );
    }

    @Test
    @DisplayName("Handle with links")
    void testHandleWithLinks() {
        when(scrapperClient.getLinks(CHAT_ID)).thenReturn(new ListLinkResponse(
            List.of(
                new LinkResponse(1L, URI.create("http://example1.com")),
                new LinkResponse(2L, URI.create("http://example2.com"))
            ),
            (int) CHAT_ID
        ));

        SendMessage result = listCommand.handle(update);

        verify(chatService).setChatState(CHAT_ID, ChatState.NONE);
        Assertions.assertEquals(CHAT_ID, result.getParameters().get("chat_id"));
        String expectedText = """
                Here are the links I am tracking:
                - http://example1.com
                - http://example2.com""";
        String expectedTextReverted = """
                Here are the links I am tracking:
                - http://example2.com
                - http://example1.com""";
        Assertions.assertTrue(expectedText.equals(result.getParameters().get("text"))
            || expectedTextReverted.equals(result.getParameters().get("text")));
    }
}
