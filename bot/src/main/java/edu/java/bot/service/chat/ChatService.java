package edu.java.bot.service.chat;

import edu.java.bot.entity.chat.ChatState;

public interface ChatService {
    void addEmptyChatWithId(Long chatId);

    void setChatState(Long chatId, ChatState state);

    ChatState getChatState(Long chatId);
}
