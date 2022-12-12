package design.patterns.course.authorization.server.domain.flow.requirements;

import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;

public interface FlowRequirement {

  public void fulfilledBy(RequestAttributes attributes);

}
