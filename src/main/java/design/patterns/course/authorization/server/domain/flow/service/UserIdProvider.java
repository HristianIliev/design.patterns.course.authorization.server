package design.patterns.course.authorization.server.domain.flow.service;

import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;

public interface UserIdProvider {

  public String getUserIdFrom(RequestAttributes attributes);

}
