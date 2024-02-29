package edu.java.scrapper.repository.link;

import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.util.Optional;

public interface LinkRepository {
    Optional<Link> findByUrl(URI url);

    void addLink(Link link);
}
