package edu.java.bot.service.chat.jdbc;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.repository.jdbc.JdbcChatStateRepository;
import edu.java.bot.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final JdbcChatStateRepository chatRepository;

    @Override
    public void addEmptyChatWithId(Long chatId) {
        chatRepository.save(
            Chat.builder()
                .id(chatId)
                .build()
        );
    }

    @Override
    public void setChatState(Long chatId, ChatState state) {
        chatRepository.setChatState(chatId, state);
    }

    @Override
    public ChatState getChatState(Long chatId) {
        return chatRepository.getChatState(chatId);
    }
}
