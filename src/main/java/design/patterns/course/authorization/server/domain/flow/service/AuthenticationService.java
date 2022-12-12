package design.patterns.course.authorization.server.domain.flow.service;

import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;

public interface AuthenticationService {

  public AuthenticationResult authenticateUserBasedOn(RequestAttributes attributes);

}
