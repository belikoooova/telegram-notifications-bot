package edu.java.bot.repository.chat;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryChatRepository implements ChatRepository {
    private final Map<Long, Chat> getChatById = new HashMap<>();

    @Override
    public void addEmptyChatWithId(Long chatId) {
        getChatById.put(chatId, new Chat(chatId));
    }

    @Override
    public void setChatState(Long chatId, ChatState state) {
        getChatById.computeIfAbsent(chatId, k -> new Chat(chatId)).setState(state);
    }

    @Override
    public ChatState getChatState(Long userId) {
        return getChatById.containsKey(userId) ? getChatById.get(userId).getState() : null;
    }
}
