package edu.java.bot.repository.jpa;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    @Transactional
    @Query("select c.state from Chat c where c.id = :chatId")
    ChatState getStateById(Long chatId);

    @Transactional
    @Modifying
    @Query("update Chat c set c.state = :state where c.id = :chatId")
    void updateStateById(Long chatId, ChatState state);
}
