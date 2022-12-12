package design.patterns.course.authorization.server.domain.entity.token.repository;

import design.patterns.course.authorization.server.domain.entity.token.Token;

import java.util.List;

public interface CleanupTokenRepository {

  public List<Token> findExpiredTokens(int batchSize);

}
