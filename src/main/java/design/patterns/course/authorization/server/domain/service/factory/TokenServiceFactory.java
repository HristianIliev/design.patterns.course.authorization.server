package design.patterns.course.authorization.server.domain.service.factory;

import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;
import design.patterns.course.authorization.server.domain.service.TokenService;

public interface TokenServiceFactory {

  public TokenService createUserCredentialsTokenService(RequestAttributes attributes);

}
