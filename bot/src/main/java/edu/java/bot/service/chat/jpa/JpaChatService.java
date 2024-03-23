package edu.java.bot.service.chat.jpa;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import edu.java.bot.repository.jpa.JpaChatRepository;
import edu.java.bot.service.chat.ChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Override
    public void addEmptyChatWithId(Long chatId) {
        chatRepository.save(
            Chat.builder()
                .id(chatId)
                .state(ChatState.NONE)
                .build()
        );
    }

    @Override
    public void setChatState(Long chatId, ChatState state) {
        chatRepository.updateStateById(chatId, state);
    }

    @Override
    public ChatState getChatState(Long chatId) {
        return chatRepository.getStateById(chatId);
    }
}
