package edu.java.scrapper.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.net.URI;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddLinkRequest {
    private URI url;
}
