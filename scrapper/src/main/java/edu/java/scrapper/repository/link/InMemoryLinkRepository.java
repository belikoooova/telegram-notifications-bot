package edu.java.scrapper.repository.link;

import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLinkRepository implements LinkRepository {
    private final Random random = new Random();
    private final Map<URI, Link> getLinkByUrl = new HashMap<>();

    @Override
    public Optional<Link> findByUrl(URI url) {
        return getLinkByUrl.containsKey(url) ? Optional.of(getLinkByUrl.get(url)) : Optional.empty();
    }

    @Override
    public Link addLink(Link link) {
        link.setId(random.nextLong());
        return getLinkByUrl.put(link.getUrl(), link);
    }

    @Override
    public Link deleteByUrl(URI url) {
        return getLinkByUrl.remove(url);
    }
}
