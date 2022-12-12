package design.patterns.course.authorization.server.domain.flow.service;

import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;

public class AuthorizationHeaderUserIdProvider implements UserIdProvider {

  @Override
  public String getUserIdFrom(RequestAttributes attributes) {
    return attributes.getUserIdFromAuthorizationHeader();
  }

}
