package design.patterns.course.authorization.server.domain.flow.impl;

import design.patterns.course.authorization.server.domain.entity.token.TokenInformation;
import design.patterns.course.authorization.server.domain.flow.Flow;
import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;
import design.patterns.course.authorization.server.domain.flow.requirements.FlowRequirement;
import design.patterns.course.authorization.server.domain.flow.requirements.RolesExistence;
import design.patterns.course.authorization.server.domain.flow.requirements.ValidUserCredentials;
import design.patterns.course.authorization.server.domain.flow.service.factory.ServiceFactory;
import design.patterns.course.authorization.server.domain.service.TokenService;
import design.patterns.course.authorization.server.domain.service.factory.TokenServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class UserCredentialsFlow implements Flow {

  private ServiceFactory serviceFactory;

  private TokenInformation token;

  public UserCredentialsFlow(ServiceFactory serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  @Override
  public void validate(RequestAttributes attributes) {
    List<FlowRequirement> requirements = new ArrayList<>();

    requirements.add(new RolesExistence(serviceFactory));
    requirements.add(new ValidUserCredentials(serviceFactory));

    for (FlowRequirement requirement : requirements) {
      requirement.fulfilledBy(attributes);
    }
  }

  @Override
  public void process(RequestAttributes attributes) {
    TokenService tokenService = createTokenIssuingService(attributes);

    token = tokenService.issueToken(attributes);
  }

  TokenService createTokenIssuingService(RequestAttributes attributes) {
    TokenServiceFactory tokenIssuingServiceFactory = serviceFactory.createTokenServiceFactory();

    return tokenIssuingServiceFactory.createUserCredentialsTokenService(attributes);
  }

  @Override
  public TokenInformation getResult() {
    return token;
  }

}
