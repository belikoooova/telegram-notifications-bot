package edu.java.bot.repository.user.state;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private Map<Long, UserState> getStateById = new HashMap<>();

    @Override
    public void setUserState(Long userId, UserState state) {
        getStateById.put(userId, state);
    }

    @Override
    public UserState getUserState(Long userId) {
        return getStateById.get(userId);
    }

    @Override
    public void clearUserState(Long userId) {
        getStateById.put(userId, UserState.NONE);
    }
}
