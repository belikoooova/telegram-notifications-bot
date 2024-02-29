package edu.java.bot.entity.user;

import edu.java.bot.entity.link.Link;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {
    @Id private Long id;
    private UserState state = UserState.NONE;
    @ManyToMany
    private Set<Link> links = new HashSet<>();

    public User(Long id) {
        this.id = id;
    }
}
