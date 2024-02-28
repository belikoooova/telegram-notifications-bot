package edu.java.bot.repository.link;

import edu.java.bot.entity.link.Link;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryLinkRepository implements LinkRepository {
    private final Map<String, Link> getLinkByUrl = new HashMap<>();
    @Override
    public void addLink(Link link) {
        getLinkByUrl.put(link.getUrl(), link);
    }
}
