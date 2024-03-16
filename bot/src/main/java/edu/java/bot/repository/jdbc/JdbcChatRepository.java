package edu.java.bot.repository.jdbc;

import edu.java.bot.entity.chat.Chat;
import java.util.List;
import edu.java.bot.entity.chat.ChatState;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository {
    private static final String ADD_QUERY = "insert into chat_state (id, state) values (?, ?::chatstate) returning *";
    private static final String DELETE_QUERY = "delete from chat_state where id=? returning *";
    private static final String FIND_ALL_QUERY = "select * from chat_state";
    private static final String SET_CHAT_STATE = "update chat_state cs set state=?::chatstate where cs.id=?";
    private static final String GET_CHAT_STATE = "select state from chat_state where id=?";
    private static final RowMapper<Chat> MAPPER = new BeanPropertyRowMapper<>(Chat.class);
    private static final RowMapper<ChatState> CHAT_STATE_MAPPER = new BeanPropertyRowMapper<>(ChatState.class);

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Chat add(Chat chat) {
        return jdbcTemplate.queryForObject(
            ADD_QUERY,
            MAPPER,
            chat.getId(),
            chat.getState().name()
        );
    }

    @Transactional
    public Chat remove(Chat chat) {
        return jdbcTemplate.queryForObject(
            DELETE_QUERY,
            MAPPER,
            chat.getId()
        );
    }

    @Transactional
    public List<Chat> findAll() {
        return jdbcTemplate.query(
            FIND_ALL_QUERY,
            MAPPER
        );
    }

    @Transactional
    public void setChatState(Long chatId, ChatState state) {
        jdbcTemplate.update(
            SET_CHAT_STATE,
            state.name(),
            chatId
        );
    }

    @Transactional
    public ChatState getChatState(Long chatId) {
        return jdbcTemplate.queryForObject(
            GET_CHAT_STATE,
            CHAT_STATE_MAPPER,
            chatId
        );
    }
}
