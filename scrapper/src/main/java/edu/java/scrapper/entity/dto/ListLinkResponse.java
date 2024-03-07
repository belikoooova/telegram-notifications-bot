package edu.java.scrapper.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class ListLinkResponse {
    private final List<LinkResponse> links;
    private final Integer size;
}
