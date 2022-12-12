package design.patterns.course.authorization.server.domain.flow.service.factory;

import design.patterns.course.authorization.server.domain.entity.token.repository.TokenRepository;
import design.patterns.course.authorization.server.domain.entity.user.repository.UserRepository;
import design.patterns.course.authorization.server.domain.flow.service.*;
import design.patterns.course.authorization.server.domain.service.CacheableUserService;
import design.patterns.course.authorization.server.domain.service.DomainUserService;
import design.patterns.course.authorization.server.domain.service.UserService;
import design.patterns.course.authorization.server.domain.service.factory.DomainTokenServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainServiceFactory implements ServiceFactory {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TokenRepository tokenRepository;

  private UserService userService;

  @Override
  public AuthenticationService createAuthenticationService() {
    return new PasswordAuthenticationService(this);
  }

  @Override
  public UserService createUserService() {
    if (userService == null) {
      userService = new CacheableUserService(new DomainUserService(userRepository));
    }

    return userService;
  }

  @Override
  public UserIdProvider createUserIdProvider() {
    return new AuthorizationHeaderUserIdProvider();
  }

  @Override
  public design.patterns.course.authorization.server.domain.service.factory.TokenServiceFactory createTokenServiceFactory() {
    return new DomainTokenServiceFactory(this, tokenRepository, userRepository);
  }

  @Override
  public AuthorizationHeaderService createAuthorizationHeaderService() {
    return new AuthorizationHeaderService();
  }

}
