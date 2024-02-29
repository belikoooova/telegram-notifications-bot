package edu.java.bot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NoExistingChatException extends RuntimeException {
    private final Long chatId;
}
