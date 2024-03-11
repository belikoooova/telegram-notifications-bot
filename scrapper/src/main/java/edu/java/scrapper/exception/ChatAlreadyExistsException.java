package edu.java.scrapper.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ChatAlreadyExistsException extends RuntimeException {
    private final Long chatId;
}
