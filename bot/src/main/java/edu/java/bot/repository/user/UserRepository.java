package edu.java.bot.repository.user;

import edu.java.bot.entity.link.Link;
import edu.java.bot.entity.user.UserState;
import java.util.Set;

public interface UserRepository {
    void addEmptyUserWithId(Long userId);
    void setUserState(Long userId, UserState state);
    UserState getUserState(Long userId);
    void clearUserState(Long userId);
    Set<Link> getUserLinks(Long userId);
    void addLink(Long userId, Link link);
}
