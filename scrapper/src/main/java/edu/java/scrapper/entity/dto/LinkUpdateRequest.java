package edu.java.scrapper.entity.dto;

import java.net.URI;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class LinkUpdateRequest {
    private final Long id;
    private final URI url;
    private final String description;
    private List<Long> tgChatIds;
}
