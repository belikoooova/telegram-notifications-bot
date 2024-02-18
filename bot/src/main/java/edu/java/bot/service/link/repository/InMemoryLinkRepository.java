package edu.java.bot.service.link.repository;

import edu.java.bot.service.link.Link;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLinkRepository implements LinkRepository {
    private Map<LinkRepositoryKey, Link> getLinkByUrl = new HashMap<>();

    @Override
    public Optional<Link> findById(LinkRepositoryKey key) {
        return getLinkByUrl.containsKey(key) ? Optional.ofNullable(getLinkByUrl.get(key)) : Optional.empty();
    }

    @Override
    public boolean existsById(LinkRepositoryKey key) {
        return false;
    }

    @Override
    public List<Link> findAll() {
        return getLinkByUrl.values().stream().toList();
    }

    @Override
    public Iterable<Link> findAllById(Iterable<LinkRepositoryKey> keys) {
        return null;
    }

    @Override
    public long count() {
        return getLinkByUrl.size();
    }

    @Override
    public void deleteById(LinkRepositoryKey key) {
        getLinkByUrl.remove(key);
    }

    @Override
    public <S extends Link> S save(S link) {
        getLinkByUrl.put(link.getKey(), link);
        return link;
    }

    @Override
    public <S extends Link> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public void delete(Link link) {

    }

    @Override
    public void deleteAllById(Iterable<? extends LinkRepositoryKey> keys) {

    }

    @Override
    public void deleteAll(Iterable<? extends Link> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
