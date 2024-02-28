package edu.java.bot.repository.user;

import edu.java.bot.entity.link.Link;
import edu.java.bot.entity.user.User;
import edu.java.bot.entity.user.UserState;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> getUserById = new HashMap<>();

    @Override
    public void addEmptyUserWithId(Long userId) {
        getUserById.put(userId, new User(userId));
    }

    @Override
    public void setUserState(Long userId, UserState state) {
        getUserById.computeIfAbsent(userId, k -> new User(userId)).setState(state);
    }

    @Override
    public UserState getUserState(Long userId) {
        return getUserById.containsKey(userId) ? getUserById.get(userId).getState() : null;
    }

    @Override
    public void clearUserState(Long userId) {
        getUserById.computeIfAbsent(userId, k -> new User(userId)).setState(UserState.NONE);
    }

    @Override
    public Set<Link> getUserLinks(Long userId) {
        return getUserById.get(userId).getLinks();
    }

    @Override
    public void addLink(Long userId, Link link) {
        getUserById.get(userId).getLinks().add(link);
    }
}
