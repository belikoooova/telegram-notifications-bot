package edu.java.scrapper.controller;

import edu.java.scrapper.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tg-chat")
public class ChatController {
    private static final String OK_POST = "Chat registered";
    private static final String OK_DELETE = "Chat successfully deleted";
    private final ChatService service;

    @PostMapping("/{id}")
    public ResponseEntity<String> registerChat(@PathVariable Long id) {
        service.addChat(id);
        return ResponseEntity.ok(OK_POST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChat(@PathVariable Long id) {
        service.deleteChat(id);
        return ResponseEntity.ok(OK_DELETE);
    }
}
