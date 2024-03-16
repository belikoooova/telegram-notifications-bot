package edu.java.bot.service.chat.jdbc.mapper;

import edu.java.bot.entity.chat.Chat;
import edu.java.bot.entity.chat.ChatState;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatMapper implements RowMapper<Chat> {
    private static final String ID = "id";
    private static final String STATE = "state";

    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Chat.builder()
            .id(rs.getLong(ID))
            .state(ChatState.valueOf(rs.getString(STATE)))
            .build();
    }
}
