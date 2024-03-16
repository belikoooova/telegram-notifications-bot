package edu.java.bot.repository.jdbc;

import edu.java.bot.entity.chat.Chat;
import java.util.List;
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
    private static final RowMapper<Chat> MAPPER = new BeanPropertyRowMapper<>(Chat.class);

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
}
