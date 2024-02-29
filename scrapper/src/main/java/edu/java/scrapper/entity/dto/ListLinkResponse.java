package edu.java.scrapper.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListLinkResponse {
    private List<LinkResponse> links;
    private Integer size;
}
