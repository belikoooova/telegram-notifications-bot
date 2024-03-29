package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.exception.LinkNotTrackedException;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@SuppressWarnings({"MultipleStringLiterals"})
public class JdbcLinkRepository {
    private static final String ADD_QUERY = """
        insert into link (url, last_checked_at)
        values (:url, :lastCheckedAt)
        returning *
        """;
    private static final String DELETE_QUERY = """
        delete from link
        where id=:id returning *
        """;

    private static final String FIND_ALL_QUERY = """
        select * from link
        """;

    private static final String FIND_ALL_LINKS_BY_CHAT_ID = """
        select * from link l
        join chat_link cl on l.id=cl.link_id
        where cl.chat_id=:chatId
        """;

    private static final String FIND_ALL_WITH_SHIFT_INTERVAL = """
        select * from link
        where last_checked_at < :newDateTime
        """;

    private static final String CONNECT_LINK_TO_CHAT = """
        insert into chat_link (link_id, chat_id)
        values (:linkId, :chatId)
        """;

    private static final String DISCONNECT_LINK_FROM_CHAT = """
        delete from chat_link
        where link_id=:linkId and chat_id=:chatId
        """;

    private static final String FIND_BY_URL = """
        select * from link
        where url=:url
        """;

    private static final String FIND_CHAT_IDS_BY_LINK_ID = """
        select cl.chat_id from chat_link cl
        where cl.link_id=:linkId
        """;

    private static final String UPDATE_LAST_CHECKED_TIME = """
        update link set last_checked_at=:dateTime where id=:id
        """;

    private static final RowMapper<Link> MAPPER = new BeanPropertyRowMapper<>(Link.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public Link save(Link link) {
        Map<String, Object> params = new HashMap<>();
        params.put("url", link.getUrl().toString());
        params.put("lastCheckedAt", link.getLastCheckedAt());
        return namedParameterJdbcTemplate.queryForObject(ADD_QUERY, params, MAPPER);
    }

    @Transactional
    public Link remove(Link link) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", link.getId());
        return namedParameterJdbcTemplate.queryForObject(DELETE_QUERY, params, MAPPER);
    }

    @Transactional
    public List<Link> findAll() {
        return namedParameterJdbcTemplate.query(FIND_ALL_QUERY, MAPPER);
    }

    @Transactional
    public List<Link> findAllLinksByChatId(long chatId) {
        Map<String, Object> params = new HashMap<>();
        params.put("chatId", chatId);
        return namedParameterJdbcTemplate.query(FIND_ALL_LINKS_BY_CHAT_ID, params, MAPPER);
    }

    @Transactional
    public void connectLinkToChat(long chatId, long linkId) {
        Map<String, Object> params = new HashMap<>();
        params.put("linkId", linkId);
        params.put("chatId", chatId);
        namedParameterJdbcTemplate.update(CONNECT_LINK_TO_CHAT, params);
    }

    @Transactional
    public Optional<Link> findLinkByUrl(URI url) {
        Map<String, Object> params = new HashMap<>();
        params.put("url", url.toString());
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(FIND_BY_URL, params, MAPPER));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Transactional
    public void disconnectLinkToChat(long chatId, long linkId) {
        Map<String, Object> params = new HashMap<>();
        params.put("linkId", linkId);
        params.put("chatId", chatId);
        int rowsAffected = namedParameterJdbcTemplate.update(DISCONNECT_LINK_FROM_CHAT, params);
        if (rowsAffected == 0) {
            throw new LinkNotTrackedException();
        }
    }

    @Transactional
    public List<Link> findAllWithShitInterval(Duration interval) {
        Map<String, Object> params = new HashMap<>();
        params.put("newDateTime", OffsetDateTime.now().minusSeconds(interval.toSeconds()));
        return namedParameterJdbcTemplate.query(FIND_ALL_WITH_SHIFT_INTERVAL, params, MAPPER);
    }

    @Transactional
    public List<Long> getChatIdsByLinkId(Long linkId) {
        Map<String, Object> params = new HashMap<>();
        params.put("linkId", linkId);
        return namedParameterJdbcTemplate.query(
            FIND_CHAT_IDS_BY_LINK_ID,
            params,
            (rs, rowNum) -> rs.getLong("chat_id")
        );
    }

    @Transactional
    public void updateLastCheckedTime(Link link, OffsetDateTime dateTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("dateTime", dateTime);
        params.put("id", link.getId());
        namedParameterJdbcTemplate.update(UPDATE_LAST_CHECKED_TIME, params);
    }
}
