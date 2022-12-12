package design.patterns.course.authorization.server.cleanup;

import design.patterns.course.authorization.server.domain.entity.token.Token;
import design.patterns.course.authorization.server.domain.entity.token.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpiredTokensCleanupJob {

  private static final int BATCH_SIZE = 50;

  @Autowired
  private TokenRepository tokenRepository;

  @Scheduled(fixedRate=60*1000)
  public void deleteExpiredTokens() {
    List<Token> expiredTokens = tokenRepository.findExpiredTokens(BATCH_SIZE);

    List<Long> expiredTokenIds = expiredTokens.stream()
        .map(Token::getId)
        .collect(Collectors.toList());

    tokenRepository.deleteAllByIdInBatch(expiredTokenIds);
  }

}
