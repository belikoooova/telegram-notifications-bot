package edu.java.scrapper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private URI url;

    public Link(URI url) {
        this.url = url;
    }
}
