package design.patterns.course.authorization.server.domain.service;

import design.patterns.course.authorization.server.domain.entity.token.Token;
import design.patterns.course.authorization.server.domain.entity.token.TokenInformation;
import design.patterns.course.authorization.server.domain.entity.token.repository.TokenRepository;
import design.patterns.course.authorization.server.domain.entity.user.User;
import design.patterns.course.authorization.server.domain.entity.user.repository.UserRepository;
import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;
import design.patterns.course.authorization.server.domain.flow.exceptions.UnexpectedFlowException;
import design.patterns.course.authorization.server.domain.flow.service.AuthenticationResult;
import design.patterns.course.authorization.server.domain.flow.service.AuthenticationService;
import design.patterns.course.authorization.server.domain.flow.service.factory.ServiceFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserCredentialsTokenService implements TokenService {

  private static final int TEN_MINUTES_IN_SEC = 10 * 60;

  private TokenRepository tokenRepository;
  private UserRepository userRepository;
  private AuthenticationService authenticationService;

  public UserCredentialsTokenService(ServiceFactory serviceFactory, TokenRepository tokenRepository, UserRepository userRepository) {
    authenticationService = serviceFactory.createAuthenticationService();

    this.tokenRepository = tokenRepository;
    this.userRepository = userRepository;
  }

  @Override
  public TokenInformation issueToken(RequestAttributes attributes) {
    AuthenticationResult authenticationResult = authenticationService.authenticateUserBasedOn(attributes);

    User user = authenticationResult.authenticatedUser();
    String requestedRoles = attributes.getRoles();

    Token token = createTokenFrom(user, requestedRoles);

    return toTokenInformation(token, user);
  }

  @Override
  public TokenInformation retrieveTokenById(String tokenId) {
    Optional<Token> optionalToken = tokenRepository.findByTokenId(tokenId);
    if (optionalToken.isEmpty()) {
      return null;
    }

    Token token = optionalToken.get();

    Optional<User> optionalUser = userRepository.findById(token.getUserId());
    if (optionalUser.isEmpty()) {
      return null;
    }

    return toTokenInformation(token, optionalUser.get());
  }

  private TokenInformation toTokenInformation(Token token, User user) {
    TokenInformation tokenInformation = new TokenInformation();

    tokenInformation.setTokenId(token.getTokenId());
    tokenInformation.setUserId(user.getUsername());
    tokenInformation.setCreatedAt(token.getCreatedAt());
    tokenInformation.setExpiresInSec(TEN_MINUTES_IN_SEC);

    String roles = token.getRoles();

    List<String> rolesList = Arrays.asList(roles.contains(" ") ? roles.split(" ") : roles.split(","));
    tokenInformation.setRoles(rolesList);

    return tokenInformation;
  }

  private Token createTokenFrom(User user, String roles) {
    Token token = new Token();

    try {
      token.setTokenId(generateTokenId());
    } catch (NoSuchAlgorithmException exception) {
      throw new UnexpectedFlowException(exception.getMessage(), exception);
    }

    token.setCreatedAt(System.currentTimeMillis());
    token.setUserId(user.getId());
    token.setExpiresInSec(TEN_MINUTES_IN_SEC);

    if (roles == null || roles.trim().isEmpty()) {
      token.setRoles(user.getRolesAsString());
    } else {
      token.setRoles(roles);
    }

    tokenRepository.save(token);

    return token;
  }

  private String generateTokenId() throws NoSuchAlgorithmException {
    MessageDigest algorithm = MessageDigest.getInstance("MD5");

    algorithm.reset();
    algorithm.update(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));

    byte[] messageDigest = algorithm.digest();

    StringBuilder hexString = new StringBuilder();
    for (int i = 0; i < messageDigest.length; i++) {
      hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
    }

    return hexString.toString();
  }

}
