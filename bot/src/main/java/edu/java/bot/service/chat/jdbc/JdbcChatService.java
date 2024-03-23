package edu.java.bot.service.chat.jdbc;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.repository.jdbc.JdbcChatRepository;
import edu.java.bot.service.chat.ChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    @Override
    public void addEmptyChatWithId(Long chatId) {
        chatRepository.add(
            Chat.builder()
                .id(chatId)
                .state(ChatState.NONE)
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
