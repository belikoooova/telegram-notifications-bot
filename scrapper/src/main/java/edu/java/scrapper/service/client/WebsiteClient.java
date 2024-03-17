package edu.java.scrapper.service.client;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import java.util.Optional;

public interface WebsiteClient {
    boolean canHandle(String url);

    Optional<LinkUpdateRequest> handle(Link link);
}
