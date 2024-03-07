package edu.java.bot.entity.dto;

import java.net.URI;
import lombok.Data;

@Data
public class RemoveLinkRequest {
    private final URI url;
}
