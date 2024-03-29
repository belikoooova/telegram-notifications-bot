package edu.java.scrapper.service.client;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.LinkUpdateRequest;
import java.util.List;

public interface WebsiteClient {
    boolean canHandle(String url);

    List<LinkUpdateRequest> handle(Link link);
}
