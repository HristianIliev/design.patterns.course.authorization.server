package design.patterns.course.authorization.server.domain.flow;

import design.patterns.course.authorization.server.domain.flow.exceptions.UnauthorizedFlowException;
import design.patterns.course.authorization.server.domain.flow.impl.UserCredentialsFlow;
import design.patterns.course.authorization.server.domain.flow.service.factory.ServiceFactory;

public class AuthorizationServerFlowFactory implements FlowFactory {

  private ServiceFactory serviceFactory;

  public AuthorizationServerFlowFactory(ServiceFactory serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  @Override
  public Flow createFlow(String flowType) {
    if (flowType == null || flowType.trim().isEmpty() || "user_credentials.".equalsIgnoreCase(flowType)) {
      return new UserCredentialsFlow(serviceFactory);
    }

    throw new UnauthorizedFlowException("Unsupported flow type");
  }

}
