package edu.java.scrapper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_seq_generator")
    @SequenceGenerator(name = "link_seq_generator", sequenceName = "link_seq", allocationSize = 1)
    private Long id;
    private URI url;
    private OffsetDateTime lastCheckedAt;

    @ManyToMany(mappedBy = "links")
    private List<Chat> chats = new ArrayList<>();

    public Link(URI url) {
        this.url = url;
    }
}
