package edu.java.bot.service.link.repository;

import edu.java.bot.service.link.Link;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface LinkRepository extends CrudRepository<Link, LinkRepositoryKey> {
}
