package design.patterns.course.authorization.server.repository;

import design.patterns.course.authorization.server.model.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Integer> {
}
