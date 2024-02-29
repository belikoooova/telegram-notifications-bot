package edu.java.scrapper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.net.URI;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    public static final Random RANDOM = new Random();
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private URI url;

    public Link(URI url) {
        new Link(RANDOM.nextLong(), url); // Temporary stub.
        // Once the JPA repository is added, the id will be generated properly.
    }
}
