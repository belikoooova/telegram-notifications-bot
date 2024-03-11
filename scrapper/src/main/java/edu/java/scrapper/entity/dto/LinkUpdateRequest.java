package edu.java.scrapper.entity.dto;

import java.net.URI;
import java.util.List;
import lombok.Data;

@Data
public class LinkUpdateRequest {
    private final Long id;
    private final URI url;
    private final String description;
    private final List<Long> tgChatIds;
}
