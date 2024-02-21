package edu.java.bot.repository.link.repository;

import edu.java.bot.repository.link.LinkResource;

public record LinkRepositoryKey(Long userId, LinkResource resource, String idOnResource) {
}
