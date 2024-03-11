package edu.java.scrapper.entity.dto;

import java.net.URI;
import lombok.Data;

@Data
public class LinkResponse {
    private final Long id;
    private final URI url;
}
