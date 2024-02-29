package edu.java.scrapper.repository.chat;

import edu.java.scrapper.entity.Chat;
import java.util.Optional;

public interface ChatRepository {
    Optional<Chat> getChatByID(Long id);
    void deleteById(Long id);
    void addChat(Chat chat);
}
