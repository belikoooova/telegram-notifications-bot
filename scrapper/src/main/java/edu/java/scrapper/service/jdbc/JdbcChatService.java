package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.exception.ChatAlreadyExistsException;
import edu.java.scrapper.exception.NoSuchChatException;
import edu.java.scrapper.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    @Override
    public void register(long tgChatId) {
        try {
            chatRepository.add(
                Chat.builder()
                    .id(tgChatId)
                    .build()
            );
        } catch (DuplicateKeyException ignored) {
            throw new ChatAlreadyExistsException(tgChatId);
        }
    }

    @Override
    public void unregister(long tgChatId) {
        try {
            chatRepository.remove(
                Chat.builder()
                    .id(tgChatId)
                    .build()
            );
        } catch (EmptyResultDataAccessException ignored) {
            throw new NoSuchChatException(tgChatId);
        }
    }
}
