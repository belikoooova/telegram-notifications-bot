package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotTrackedException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;

    @Override
    @Transactional
    public Link add(long tgChatId, URI url) {
        Link link = linkRepository.findLinkByUrl(url)
            .orElse(
                linkRepository.save(Link.builder()
                    .url(url)
                    .lastCheckedAt(OffsetDateTime.now())
                    .build()
                )
            );

        try {
            linkRepository.connectLinkToChat(tgChatId, link.getId());
        } catch (DuplicateKeyException ignored) {
            throw new LinkAlreadyTrackedException();
        }

        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, URI url) {
        Link link = linkRepository.findLinkByUrl(url)
            .orElseThrow(NoSuchLinkException::new);

        try {
            linkRepository.disconnectLinkToChat(tgChatId, link.getId());
        } catch (EmptyResultDataAccessException ignored) {
            throw new LinkNotTrackedException();
        }

        return link;
    }

    @Override
    public Collection<Link> listAll(long tgChatId) {
        List<Link> links = linkRepository.findAllLinksByChatId(tgChatId);
        return links == null ? Collections.emptyList() : links;
    }

    @Override
    public Collection<Link> listAllOldChecked(Duration interval) {
        return linkRepository.findAllWithShitInterval(interval);
    }

    @Override
    public List<Long> listChatIdsByLinkId(Long linkId) {
        return linkRepository.getChatIdsByLinkId(linkId);
    }

    @Override
    public void updateLastCheckedTime(Link link, OffsetDateTime dateTime) {
        linkRepository.updateLastCheckedTime(link, dateTime);
    }
}
