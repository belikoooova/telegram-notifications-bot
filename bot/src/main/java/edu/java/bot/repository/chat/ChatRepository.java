package edu.java.bot.repository.chat;

import edu.java.bot.entity.chat.ChatState;

public interface ChatRepository {
    void addEmptyChatWithId(Long chatId);

    void setChatState(Long chatId, ChatState state);

    ChatState getChatState(Long chatId);
}
