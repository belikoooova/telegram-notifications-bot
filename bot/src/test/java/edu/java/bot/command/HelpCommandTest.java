package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.repository.chat.ChatRepository;
import edu.java.bot.service.command.Command;
import edu.java.bot.service.command.HelpCommand;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HelpCommandTest {
    private static final long CHAT_ID = 123L;

    @Mock
    private ChatRepository chatRepository = mock(ChatRepository.class);

    @Mock
    private Command command1, command2;

    @InjectMocks
    private HelpCommand helpCommand;

    Message message = mock(Message.class);
    Chat chat = mock(Chat.class);
    private Update mockUpdate;

    @BeforeEach
    void setUp() {
        mockUpdate = mock(Update.class);
        command1 = mock(Command.class);
        command2 = mock(Command.class);

        when(mockUpdate.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);

        when(command1.command()).thenReturn("command1");
        when(command1.description()).thenReturn("Description1");
        when(command2.command()).thenReturn("command2");
        when(command2.description()).thenReturn("Description2");

        List<Command> commands = Arrays.asList(command1, command2);
        helpCommand = new HelpCommand(commands, chatRepository);
    }

    @Test
    @DisplayName("Help Command Handles User Request")
    void testHelpCommandHandle() {
        SendMessage response = helpCommand.handle(mockUpdate);

        verify(chatRepository).setChatState(CHAT_ID, ChatState.NONE);
        assertEquals(CHAT_ID, response.getParameters().get("chat_id"));

        String expectedMessage = """
                Here are the commands I can perform:
                command1 -> Description1;
                command2 -> Description2""";

        assertEquals(expectedMessage, response.getParameters().get("text"));
    }
}
