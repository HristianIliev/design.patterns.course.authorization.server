package design.patterns.course.authorization.server.domain.entity.token.repository;

import design.patterns.course.authorization.server.domain.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>, CleanupTokenRepository {

  public Optional<Token> findByTokenId(String tokenId);

}
