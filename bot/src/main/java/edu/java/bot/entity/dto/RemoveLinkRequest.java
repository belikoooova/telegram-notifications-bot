package edu.java.bot.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveLinkRequest {
    private URI url;
}
