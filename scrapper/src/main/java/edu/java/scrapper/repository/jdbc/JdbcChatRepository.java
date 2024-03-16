package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Chat;
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
    private static final String ADD_QUERY = "insert into chat (id) values (?) returning *";
    private static final String DELETE_QUERY = "delete from chat where id=? returning *";
    private static final String FIND_ALL_QUERY = "select * from chat";
    private static final RowMapper<Chat> CHAT_MAPPER = new BeanPropertyRowMapper<>(Chat.class);

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Chat add(Chat chat) {
        return jdbcTemplate.queryForObject(
            ADD_QUERY,
            CHAT_MAPPER,
            chat.getId()
        );
    }

    @Transactional
    public Chat remove(Chat chat) {
        return jdbcTemplate.queryForObject(
            DELETE_QUERY,
            CHAT_MAPPER,
            chat.getId()
        );
    }

    @Transactional
    public List<Chat> findAll() {
        return jdbcTemplate.query(
            FIND_ALL_QUERY,
            CHAT_MAPPER
        );
    }
}
