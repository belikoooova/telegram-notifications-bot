package edu.java.scrapper.entity;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link {
    private Long id;
    private URI url;
    private OffsetDateTime lastCheckedAt;

    public Link(URI url) {
        this.url = url;
    }
}
