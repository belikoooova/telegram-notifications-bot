package edu.java.scrapper.entity.dto;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class AddLinkRequest {
    private final URI url;
}
