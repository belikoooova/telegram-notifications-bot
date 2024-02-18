package edu.java.bot.service.user;

import edu.java.bot.service.user.state.UserState;
import jakarta.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id private Long id;
    private UserState state;
}
