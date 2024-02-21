package edu.java.bot.repository.link.repository;

import edu.java.bot.repository.link.Link;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LinkRepository extends CrudRepository<Link, LinkRepositoryKey> {
}
