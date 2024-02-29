package edu.java.scrapper.repository.chat;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.repository.chat.ChatRepository;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryChatRepository implements ChatRepository {
    private final Map<Long, Chat> getChatById = new HashMap<>();

    @Override
    public Optional<Chat> getChatByID(Long id) {
        return getChatById.containsKey(id) ? Optional.of(getChatById.get(id)) : Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        getChatById.remove(id);
    }

    @Override
    public void addChat(Chat chat) {
        getChatById.put(chat.getId(), chat);
    }
}
