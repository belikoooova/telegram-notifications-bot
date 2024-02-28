package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.link.Link;
import edu.java.bot.repository.link.LinkRepository;
import edu.java.bot.repository.user.UserRepository;
import edu.java.bot.service.command.ListCommand;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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
    private static final long USER_ID = 1;
    private static final long NO_LINKS = 0;
    private static final long SOME_LINKS = 2;

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ListCommand listCommand;
    private Update update = mock(Update.class);
    private Message message = mock(Message.class);
    private Chat chat = mock(Chat.class);
    private User user = mock(User.class);

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        userRepository = mock(UserRepository.class);
        listCommand = new ListCommand(userRepository);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);
        when(message.from()).thenReturn(user);
        when(user.id()).thenReturn(USER_ID);
    }

    @Test
    @DisplayName("Handle without links")
    void testHandleNoLinks() {
        // userRepository.getUserLinks(userId).isEmpty()
        when(userRepository.getUserLinks(USER_ID)).thenReturn(new HashSet<>());

        SendMessage result = listCommand.handle(update);

        verify(userRepository).clearUserState(USER_ID);
        Assertions.assertEquals(CHAT_ID, result.getParameters().get("chat_id"));
        Assertions.assertEquals(
            "Sorry, the list of tracked links is empty.",
            result.getParameters().get("text")
        );
    }

    @Test
    @DisplayName("Handle with links")
    void testHandleWithLinks() {
        Link link1 = mock(Link.class);
        Link link2 = mock(Link.class);
        when(link1.getUrl()).thenReturn("http://example1.com");
        when(link2.getUrl()).thenReturn("http://example2.com");
        when(userRepository.getUserLinks(USER_ID)).thenReturn(Set.of(link1, link2));

        SendMessage result = listCommand.handle(update);

        verify(userRepository).clearUserState(USER_ID);
        Assertions.assertEquals(CHAT_ID, result.getParameters().get("chat_id"));
        String expectedText = "Here are the links I am tracking:" +
            "\n- " +
            "http://example1.com" +
            "\n- " +
            "http://example2.com";
        Assertions.assertEquals(expectedText, result.getParameters().get("text"));
    }
}
