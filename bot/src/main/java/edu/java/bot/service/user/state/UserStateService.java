package edu.java.bot.service.user.state;

public interface UserStateService {
    void setUserState(Long userId, UserState state);

    UserState getUserState(Long userId);

    void clearUserState(Long userId);
}
