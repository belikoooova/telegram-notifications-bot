package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.command.Command;
import edu.java.bot.service.processor.ProcessorHumanReadableMessage;
import edu.java.bot.service.processor.SimpleUserMessageProcessor;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleUserMessageProcessorTest {
    private static final long CHAT_ID = 123L;
    private Command commandOne = mock(Command.class);
    private Command commandTwo = mock(Command.class);

    @InjectMocks
    private SimpleUserMessageProcessor userMessageProcessor;
    private Update update = mock(Update.class);
    private Message message = mock(Message.class);
    private Chat chat = mock(Chat.class);

    @BeforeEach
    void setUp() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(CHAT_ID);

        when(commandOne.supports(update)).thenReturn(false);
        when(commandTwo.supports(update)).thenReturn(true);
        when(commandTwo.handle(update)).thenReturn(new SendMessage(CHAT_ID, "Handled by commandTwo"));

        List<Command> commands = Arrays.asList(commandOne, commandTwo);
        userMessageProcessor = new SimpleUserMessageProcessor(commands);
    }

    @Test
    @DisplayName("Process should return error message when no command supports the update")
    void processShouldReturnErrorMessageWhenNoCommandSupportsTheUpdate() {
        when(commandOne.supports(update)).thenReturn(false);
        when(commandTwo.supports(update)).thenReturn(false);

        SendMessage response = userMessageProcessor.process(update);

        assertEquals(
            ProcessorHumanReadableMessage.ERROR_UNKNOWN_COMMAND_OR_INCORRECT_TEXT.toString(),
            response.getParameters().get("text")
        );
    }
}
