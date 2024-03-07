package edu.java.scrapper.controller;

import edu.java.scrapper.entity.dto.AddLinkRequest;
import edu.java.scrapper.entity.dto.ListLinkResponse;
import edu.java.scrapper.entity.dto.RemoveLinkRequest;
import edu.java.scrapper.service.link.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinkController {
    private static final String OK_POST = "Link successfully added";
    private static final String OK_DELETE = "Link successfully deleted";
    private final LinkService service;

    @GetMapping("/{chatId}")
    ResponseEntity<ListLinkResponse> getAllLinks(@PathVariable Long chatId) {
        return ResponseEntity.ok(service.getAllLinks(chatId));
    }

    @PostMapping("/{chatId}")
    ResponseEntity<String> trackLink(@PathVariable Long chatId, @RequestBody AddLinkRequest request) {
        service.addLink(chatId, request);
        return ResponseEntity.ok(OK_POST);
    }

    @DeleteMapping("/{chatId}")
    ResponseEntity<String> deleteLink(@PathVariable Long chatId, @RequestBody RemoveLinkRequest request) {
        service.deleteLink(chatId, request);
        return ResponseEntity.ok(OK_DELETE);
    }
}
