package design.patterns.course.authorization.server.domain.entity.token.repository;

import design.patterns.course.authorization.server.domain.entity.token.Token;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class CleanupTokenRepositoryImpl implements CleanupTokenRepository{

  private static final String EXPIRED_TOKENS_QUERY =
      "SELECT * FROM authorization_server.token T " +
          "WHERE " +
            "(T.createdAt + T.expiresInSec * 1000) < ? " +
          "LIMIT ?";

  @Autowired
  private EntityManager entityManager;

  @Override
  public List<Token> findExpiredTokens(int batchSize) {
    Query query = entityManager.createNativeQuery(EXPIRED_TOKENS_QUERY, Token.class);
    query.setParameter(1, System.currentTimeMillis());
    query.setParameter(2, batchSize);

    return query.getResultList();
  }

}
