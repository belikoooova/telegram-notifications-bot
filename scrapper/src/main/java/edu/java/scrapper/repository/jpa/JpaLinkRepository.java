package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    @Transactional
    @Query("from Link where url = :url")
    @EntityGraph(attributePaths = {"chats"})
    Optional<Link> findByUrl(URI url);

    @Transactional
    @Modifying
    @Query("update Link l set l.lastCheckedAt = :dateTime where l.id = :id")
    void updateLastCheckedTime(Long id, OffsetDateTime dateTime);

    @Transactional
    @Query("select l from Link l where l.lastCheckedAt < :newDateTime")
    List<Link> findAllWithShiftInterval(OffsetDateTime newDateTime);

    @Transactional
    @EntityGraph(attributePaths = {"chats"})
    @Query("select l from Link l join l.chats c where c.id = :chatId")
    List<Link> findAllLinksByChatId(Long chatId);

    @Transactional
    @Query("select c.id from Chat c join c.links l where l.id = :linkId")
    List<Long> findChatIdsByLinkId(Long linkId);
}
