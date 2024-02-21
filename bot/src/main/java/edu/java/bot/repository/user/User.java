package edu.java.bot.repository.user;

import edu.java.bot.repository.user.state.UserState;
import jakarta.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id private Long id;
    private UserState state;
}
