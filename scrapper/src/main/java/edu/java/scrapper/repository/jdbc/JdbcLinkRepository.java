package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.exception.LinkNotTrackedException;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository {
    private static final String ADD_QUERY = "insert into link (url, last_checked_at, last_updated_at) "
        + "values (?, ?, ?) returning *";
    private static final String DELETE_QUERY = "delete from link where id=? returning *";
    private static final String FIND_ALL_QUERY = "select * from link";
    private static final String FIND_ALL_LINKS_BY_CHAT_ID = "select * from link l "
        + "join chat_link cl on l.id=cl.link_id "
        + "where cl.chat_id=?";
    private static final String FIND_ALL_WITH_SHIFT_INTERVAL = "select * from link "
        + "where last_checked_at < ?";
    private static final String CONNECT_LINK_TO_CHAT = "insert into chat_link (link_id, chat_id) values (?, ?)";
    private static final String DISCONNECT_LINK_FROM_CHAT = "delete from chat_link where link_id=? and chat_id=?";
    private static final String FIND_BY_URL = "select * from link where url=?";
    private static final String FIND_CHAT_IDS_BY_LINK_ID = "select cl.chat_id from chat_link cl where cl.link_id=?";
    private static final RowMapper<Link> MAPPER = new BeanPropertyRowMapper<>(Link.class);

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Link add(Link link) {
        return jdbcTemplate.queryForObject(
            ADD_QUERY,
            MAPPER,
            link.getUrl().toString(),
            link.getLastCheckedAt(),
            link.getLastUpdatedAt()
        );
    }

    @Transactional
    public Link remove(Link link) {
        return jdbcTemplate.queryForObject(
            DELETE_QUERY,
            MAPPER,
            link.getId()
        );
    }

    @Transactional
    public List<Link> findAll() {
        return jdbcTemplate.query(
            FIND_ALL_QUERY,
            MAPPER
        );
    }

    @Transactional
    public List<Link> findAllLinksByChatId(long chatId) {
        return jdbcTemplate.query(
            FIND_ALL_LINKS_BY_CHAT_ID,
            MAPPER,
            chatId
        );
    }

    @Transactional
    public void connectLinkToChat(long chatId, long linkId) {
        jdbcTemplate.update(
            CONNECT_LINK_TO_CHAT,
            linkId,
            chatId
        );
    }

    @Transactional
    public Optional<Link> findLinkByUrl(URI url) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                FIND_BY_URL,
                MAPPER,
                url.toString()
            ));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Transactional
    public void disconnectLinkToChat(long chatId, long linkId) {
        int rowsAffected = jdbcTemplate.update(
            DISCONNECT_LINK_FROM_CHAT,
            linkId,
            chatId
        );
        if (rowsAffected == 0) {
            throw new LinkNotTrackedException();
        }
    }

    @Transactional
    public List<Link> findAllWithShitInterval(Duration interval) {
        OffsetDateTime newDateTime = OffsetDateTime.now().minusSeconds(interval.toSeconds());
        return jdbcTemplate.query(
            FIND_ALL_WITH_SHIFT_INTERVAL,
            MAPPER,
            newDateTime
        );
    }

    @Transactional
    public List<Long> getChatIdsByLinkId(Long linkId) {
        return jdbcTemplate.query(
            FIND_CHAT_IDS_BY_LINK_ID,
            (rs, rowNum) -> rs.getLong("chat_id"),
            linkId
        );
    }
}
