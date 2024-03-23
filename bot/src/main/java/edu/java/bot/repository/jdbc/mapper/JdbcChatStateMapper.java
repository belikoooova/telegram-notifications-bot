package edu.java.bot.repository.jdbc.mapper;

import edu.java.bot.entity.chat.ChatState;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatStateMapper implements RowMapper<ChatState> {
    private static final String STATE = "state";

    @Override
    public ChatState mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ChatState.valueOf(rs.getString(STATE));
    }
}
