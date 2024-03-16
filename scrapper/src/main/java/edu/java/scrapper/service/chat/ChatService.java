package edu.java.scrapper.service.chat;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatAlreadyExistsException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository repository;

    public void addChat(Long id) {
        if (repository.getChatByID(id).isPresent()) {
            throw new ChatAlreadyExistsException(id);
        }
        repository.addChat(new Chat(id));
    }

    public void deleteChat(Long id) {
        if (repository.getChatByID(id).isEmpty()) {
            throw new NoSuchChatException(id);
        }
        repository.deleteById(id);
    }
}
