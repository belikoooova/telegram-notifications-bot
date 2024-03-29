package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Chat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository {
    private static final String ADD_QUERY = "insert into chat (id) values (:id) returning *";
    private static final String DELETE_QUERY = "delete from chat where id=:id returning *";
    private static final String FIND_ALL_QUERY = "select * from chat";
    private static final RowMapper<Chat> CHAT_MAPPER = new BeanPropertyRowMapper<>(Chat.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public Chat save(Chat chat) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", chat.getId());

        return namedParameterJdbcTemplate.queryForObject(
            ADD_QUERY,
            paramMap,
            CHAT_MAPPER
        );
    }

    @Transactional
    public Chat remove(Chat chat) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", chat.getId());

        return namedParameterJdbcTemplate.queryForObject(
            DELETE_QUERY,
            paramMap,
            CHAT_MAPPER
        );
    }

    @Transactional
    public List<Chat> findAll() {
        return namedParameterJdbcTemplate.query(
            FIND_ALL_QUERY,
            new HashMap<>(),
            CHAT_MAPPER
        );
    }
}
