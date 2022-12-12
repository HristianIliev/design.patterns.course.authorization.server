package design.patterns.course.authorization.server.domain.service.factory;

import design.patterns.course.authorization.server.domain.entity.token.repository.TokenRepository;
import design.patterns.course.authorization.server.domain.entity.user.repository.UserRepository;
import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;
import design.patterns.course.authorization.server.domain.flow.service.factory.ServiceFactory;
import design.patterns.course.authorization.server.domain.service.TokenService;
import design.patterns.course.authorization.server.domain.service.UserCredentialsTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainTokenServiceFactory implements TokenServiceFactory {

  private TokenRepository tokenRepository;
  private UserRepository userRepository;
  private ServiceFactory serviceFactory;

  @Autowired
  public DomainTokenServiceFactory(ServiceFactory serviceFactory, TokenRepository tokenRepository, UserRepository userRepository) {
    this.serviceFactory = serviceFactory;
    this.tokenRepository = tokenRepository;
    this.userRepository = userRepository;
  }

  @Override
  public TokenService createUserCredentialsTokenService(RequestAttributes attributes) {
    return new UserCredentialsTokenService(serviceFactory, tokenRepository, userRepository);
  }

}
