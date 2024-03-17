package edu.java.scrapper.service;

import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, URI url);

    Link remove(long tgChatId, URI url);

    Collection<Link> listAll(long tgChatId);

    Collection<Link> listAllOldChecked(Duration interval);

    List<Long> listChatIdsByLinkId(Long linkId);
}
