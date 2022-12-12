package design.patterns.course.authorization.server.domain.flow.service.factory;

import design.patterns.course.authorization.server.domain.flow.service.AuthenticationService;
import design.patterns.course.authorization.server.domain.flow.service.AuthorizationHeaderService;
import design.patterns.course.authorization.server.domain.flow.service.UserIdProvider;
import design.patterns.course.authorization.server.domain.service.UserService;
import design.patterns.course.authorization.server.domain.service.factory.TokenServiceFactory;

public interface ServiceFactory {

  public AuthenticationService createAuthenticationService();

  public UserService createUserService();

  public UserIdProvider createUserIdProvider();

  public TokenServiceFactory createTokenServiceFactory();

  public AuthorizationHeaderService createAuthorizationHeaderService();

}
