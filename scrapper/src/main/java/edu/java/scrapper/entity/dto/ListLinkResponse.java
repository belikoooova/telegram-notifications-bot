package edu.java.scrapper.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ListLinkResponse {
    private final List<LinkResponse> links;
    private final Integer size;
}
