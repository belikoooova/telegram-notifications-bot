package edu.java.bot.entity.dto;

import java.util.List;
import lombok.Data;

@Data
public class ListLinkResponse {
    private final List<LinkResponse> links;
    private final Integer size;
}
