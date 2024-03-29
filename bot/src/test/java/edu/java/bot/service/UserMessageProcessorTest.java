package edu.java.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.command.Command;
import edu.java.bot.service.processor.UserMessageProcessor;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserMessageProcessorTest {
    private static final long CHAT_ID = 123L;
    private final Command commandOne = mock(Command.class);
    private final Command commandTwo = mock(Command.class);

    @InjectMocks
    private UserMessageProcessor userMessageProcessor;
    private final Update update = mock(Update.class);
    private final Message message = mock(Message.class);
    private final Chat chat = mock(Chat.class);

    @BeforeEach
    void setUp() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);

        when(commandOne.supports(update)).thenReturn(false);
        when(commandTwo.supports(update)).thenReturn(true);
        when(commandTwo.handle(update)).thenReturn(new SendMessage(CHAT_ID, "Handled by commandTwo"));

        List<Command> commands = Arrays.asList(commandOne, commandTwo);
        userMessageProcessor = new UserMessageProcessor(commands);
    }

    @Test
    @DisplayName("Process should return error message when no command supports the update")
    void processShouldReturnErrorMessageWhenNoCommandSupportsTheUpdate() {
        when(commandOne.supports(update)).thenReturn(false);
        when(commandTwo.supports(update)).thenReturn(false);

        SendMessage response = userMessageProcessor.process(update);

        assertEquals(
            "Sorry, you entered an unknown command or an incorrect message.",
            response.getParameters().get("text")
        );
    }
}
