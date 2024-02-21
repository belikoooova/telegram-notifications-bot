package edu.java.bot.repository.user.state;

public interface UserRepository {
    void setUserState(Long userId, UserState state);

    UserState getUserState(Long userId);

    void clearUserState(Long userId);
}
