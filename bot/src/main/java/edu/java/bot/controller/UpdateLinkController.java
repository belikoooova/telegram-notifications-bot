package edu.java.bot.controller;

import edu.java.bot.entity.dto.LinkUpdateRequest;
import edu.java.bot.service.processor.LinkUpdateProcessor;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateLinkController {
    private static final String OK_DESCRIPTION = "Update processed";
    private final LinkUpdateProcessor processor;

    @PostMapping(value = "/updates",
                 consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> sendUpdate(@Valid @RequestBody LinkUpdateRequest request) {
        processor.process(request);
        return ResponseEntity.ok(OK_DESCRIPTION);
    }
}
