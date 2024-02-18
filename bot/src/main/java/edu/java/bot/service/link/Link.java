package edu.java.bot.service.link;

import edu.java.bot.service.link.repository.LinkRepositoryKey;
import java.net.URI;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Link {
    private URI url;
    private Long userId;
    private LinkResource resource;
    private String idOnResource;
    private final @Id LinkRepositoryKey key;

    public Link(LinkRepositoryKey key) {
        this.key = key;
    }
}
