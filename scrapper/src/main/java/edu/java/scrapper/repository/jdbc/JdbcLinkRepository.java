package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Link;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository {
    private static final String ADD_QUERY = "insert into link (url, last_checked_at) values (?, ?) returning *";
    private static final String DELETE_QUERY = "delete from link where id=? returning *";
    private static final String FIND_ALL_QUERY = "select * from link";
    private static final RowMapper<Link> MAPPER = new BeanPropertyRowMapper<>(Link.class);

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Link add(Link link) {
        return jdbcTemplate.queryForObject(
            ADD_QUERY,
            MAPPER,
            link.getUrl().toString(),
            link.getLastCheckedAt()
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
}
