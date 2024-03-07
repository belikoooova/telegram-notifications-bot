package edu.java.bot.entity.dto;

import lombok.Data;

import java.net.URI;
import java.util.List;

@Data
public class LinkUpdateRequest {
    private final Long id;
    private final URI url;
    private final String description;
    private final List<Long> tgChatIds;
}
