package edu.java.scrapper.service.jpa;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatAlreadyExistsException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Override
    public void register(long tgChatId) {
        if (chatRepository.existsById(tgChatId)) {
            throw new ChatAlreadyExistsException(tgChatId);
        }
        chatRepository.save(
            Chat.builder()
                .id(tgChatId)
                .build()
        );
    }

    @Override
    public void unregister(long tgChatId) {
        if (!chatRepository.existsById(tgChatId)) {
            throw new NoSuchChatException(tgChatId);
        }
        chatRepository.deleteById(tgChatId);
    }
}
