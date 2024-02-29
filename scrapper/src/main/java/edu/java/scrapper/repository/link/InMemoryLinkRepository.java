package edu.java.scrapper.repository.link;

import edu.java.scrapper.entity.Link;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryLinkRepository implements LinkRepository {
    private final Map<URI, Link> getLinkByUrl = new HashMap<>();
    @Override
    public Optional<Link> findByUrl(URI url) {
        return getLinkByUrl.containsKey(url) ? Optional.of(getLinkByUrl.get(url)) : Optional.empty();
    }

    @Override
    public void addLink(Link link) {
        getLinkByUrl.put(link.getUrl(), link);
    }
}
