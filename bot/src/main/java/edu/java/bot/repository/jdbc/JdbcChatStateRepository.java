package edu.java.bot.repository.jdbc;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatStateRepository {
    private static final String ADD_QUERY =
        "insert into chat_state (id, state) values (:id, :state::chatstate) returning *";
    private static final String DELETE_QUERY = "delete from chat_state where id=:id returning *";
    private static final String FIND_ALL_QUERY = "select * from chat_state";
    private static final String SET_CHAT_STATE = "update chat_state cs set state=:state::chatstate where cs.id=:id";
    private static final String GET_CHAT_STATE = "select state from chat_state where id=:id";
    private static final String STATE = "state";

    private final RowMapper<Chat> mapper;
    private final RowMapper<ChatState> chatStateMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public Chat save(Chat chat) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", chat.getId());
        params.put(STATE, chat.getState().name());

        return namedParameterJdbcTemplate.queryForObject(
            ADD_QUERY,
            params,
            mapper
        );
    }

    @Transactional
    public Chat remove(Chat chat) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", chat.getId());

        return namedParameterJdbcTemplate.queryForObject(
            DELETE_QUERY,
            params,
            mapper
        );
    }

    @Transactional
    public List<Chat> findAll() {
        return namedParameterJdbcTemplate.query(
            FIND_ALL_QUERY,
            mapper
        );
    }

    @Transactional
    public void setChatState(Long chatId, ChatState state) {
        Map<String, Object> params = new HashMap<>();
        params.put(STATE, state.name());
        params.put("id", chatId);

        namedParameterJdbcTemplate.update(
            SET_CHAT_STATE,
            params
        );
    }

    @Transactional
    public ChatState getChatState(Long chatId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", chatId);

        return namedParameterJdbcTemplate.queryForObject(
            GET_CHAT_STATE,
            params,
            chatStateMapper
        );
    }
}
