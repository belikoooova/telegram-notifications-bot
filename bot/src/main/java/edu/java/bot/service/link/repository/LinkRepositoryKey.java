package edu.java.bot.service.link.repository;

import edu.java.bot.service.link.LinkResource;

public record LinkRepositoryKey(Long userId, LinkResource resource, String idOnResource) {
}
