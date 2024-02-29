package edu.java.scrapper.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.net.URI;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LinkResponse {
    private Long id;
    private URI url;
}
