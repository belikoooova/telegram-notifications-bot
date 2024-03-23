package edu.java.scrapper.service.jpa;

import edu.java.scrapper.entity.Chat;
import edu.java.scrapper.entity.Link;
import edu.java.scrapper.exception.LinkAlreadyTrackedException;
import edu.java.scrapper.exception.LinkNotTrackedException;
import edu.java.scrapper.exception.NoSuchLinkException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.service.LinkService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Override
    public Link add(long tgChatId, URI url) {
        Link link = linkRepository.findByUrl(url)
            .orElse(
                linkRepository.save(Link.builder()
                    .url(url)
                    .lastCheckedAt(OffsetDateTime.now())
                    .build()
                )
            );
        Chat chat = chatRepository.getReferenceById(tgChatId);

        if (chat.getLinks().contains(link)) {
            throw new LinkAlreadyTrackedException();
        }
        chat.getLinks().add(link);
        link.getChats().add(chat);

        chatRepository.save(chat);
        linkRepository.save(link);
        return link;
    }

    @Override
    public Link remove(long tgChatId, URI url) {
        Link link = linkRepository.findByUrl(url)
            .orElseThrow(NoSuchLinkException::new);

        Chat chat = chatRepository.getReferenceById(tgChatId);

        boolean isRemovedFromChat = chat.getLinks().remove(link);
        boolean isRemovedFromLink = link.getChats().remove(chat);

        if (!isRemovedFromChat) {
            throw new LinkNotTrackedException();
        }

        chatRepository.save(chat);
        if (isRemovedFromLink) {
            linkRepository.save(link);
        }

        return link;
    }

    @Override
    public Collection<Link> listAll(long tgChatId) {
        return linkRepository.findAll();
    }

    @Override
    public Collection<Link> listAllOldChecked(Duration interval) {
        return linkRepository.findAllWithShiftInterval(OffsetDateTime.now().minusSeconds(interval.toSeconds()));
    }

    @Override
    public List<Long> listChatIdsByLinkId(Long linkId) {
        return linkRepository.findChatIdsByLinkId(linkId);
    }

    @Override
    public void updateLastCheckedTime(Link link, OffsetDateTime dateTime) {
        linkRepository.updateLastCheckedTime(link.getId(), dateTime);
    }
}
