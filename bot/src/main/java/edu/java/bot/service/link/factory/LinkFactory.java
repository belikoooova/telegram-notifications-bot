package edu.java.bot.service.link.factory;

import edu.java.bot.service.link.Link;
import edu.java.bot.service.link.repository.LinkRepositoryKey;
import java.net.URISyntaxException;

public interface LinkFactory {
    Link create(Long userId, String urlString) throws URISyntaxException;

    LinkRepositoryKey createKey(Long userId, String urlString);
}
