package edu.java.scrapper.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NoSuchChatException extends RuntimeException {
    private final Long chatId;
}
