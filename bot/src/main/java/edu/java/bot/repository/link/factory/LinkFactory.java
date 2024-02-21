package edu.java.bot.repository.link.factory;

import edu.java.bot.repository.link.Link;
import edu.java.bot.repository.link.repository.LinkRepositoryKey;
import java.net.URISyntaxException;

public interface LinkFactory {
    Link create(Long userId, String urlString) throws URISyntaxException;

    LinkRepositoryKey createKey(Long userId, String urlString);
}
