package design.patterns.course.authorization.server.domain.flow.requirements;

import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;
import design.patterns.course.authorization.server.domain.flow.exceptions.UnauthorizedFlowException;
import design.patterns.course.authorization.server.domain.flow.service.AuthenticationResult;
import design.patterns.course.authorization.server.domain.flow.service.AuthenticationService;
import design.patterns.course.authorization.server.domain.flow.service.factory.ServiceFactory;

public class ValidUserCredentials implements FlowRequirement {

  private ServiceFactory serviceFactory;

  public ValidUserCredentials(ServiceFactory serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  @Override
  public void fulfilledBy(RequestAttributes attributes) {
    AuthenticationService authenticationService = serviceFactory.createAuthenticationService();

    AuthenticationResult authenticationResult = authenticationService.authenticateUserBasedOn(attributes);
    if (authenticationResult.isAuthenticationFailed()) {
      throw new UnauthorizedFlowException("Wrong credentials.");
    }
  }

}
