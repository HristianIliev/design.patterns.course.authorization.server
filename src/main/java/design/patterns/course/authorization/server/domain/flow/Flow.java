package design.patterns.course.authorization.server.domain.flow;

import design.patterns.course.authorization.server.domain.entity.token.TokenInformation;
import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;

public interface Flow {

  public void validate(RequestAttributes attributes);

  public void process(RequestAttributes attributes);

  public TokenInformation getResult();

}
