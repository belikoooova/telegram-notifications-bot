package edu.java.scrapper.controller;

import edu.java.scrapper.entity.Link;
import edu.java.scrapper.entity.dto.AddLinkRequest;
import edu.java.scrapper.entity.dto.LinkResponse;
import edu.java.scrapper.entity.dto.ListLinkResponse;
import edu.java.scrapper.entity.dto.RemoveLinkRequest;
import edu.java.scrapper.service.LinkService;
import java.util.Collection;
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
    private final LinkService linkService;

    @GetMapping("/{chatId}")
    public ResponseEntity<ListLinkResponse> getAllLinks(@PathVariable Long chatId) {
        Collection<Link> links = linkService.listAll(chatId);
        return ResponseEntity.ok(
            ListLinkResponse.builder()
                .links(
                    links.stream()
                    .map(
                        l -> LinkResponse.builder()
                        .id(l.getId())
                        .url(l.getUrl())
                        .build()
                    )
                    .toList()
                )
                .size(links.size())
                .build()
        );
        //return ResponseEntity.ok(serviceDepr.getAllLinks(chatId));
    }

    @PostMapping("/{chatId}")
    public ResponseEntity<String> trackLink(@PathVariable Long chatId, @RequestBody AddLinkRequest request) {
        linkService.add(chatId, request.getUrl());
        // serviceDepr.addLink(chatId, request);
        return ResponseEntity.ok(OK_POST);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteLink(@PathVariable Long chatId, @RequestBody RemoveLinkRequest request) {
        linkService.remove(chatId, request.getUrl());
        //serviceDepr.deleteLink(chatId, request);
        return ResponseEntity.ok(OK_DELETE);
    }
}
