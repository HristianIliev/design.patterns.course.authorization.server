package design.patterns.course.authorization.server.domain.flow.service;

import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;
import design.patterns.course.authorization.server.domain.flow.exceptions.UnexpectedFlowException;
import design.patterns.course.authorization.server.domain.flow.service.factory.ServiceFactory;
import design.patterns.course.authorization.server.domain.service.UserService;

public class PasswordAuthenticationService implements AuthenticationService {

  private UserService userService;
  private AuthorizationHeaderService authorizationHeaderService;

  public PasswordAuthenticationService(ServiceFactory serviceFactory) {
    userService = serviceFactory.createUserService();
    authorizationHeaderService = serviceFactory.createAuthorizationHeaderService();
  }

  @Override
  public AuthenticationResult authenticateUserBasedOn(RequestAttributes attributes) {
    String authorizationHeader = attributes.getAuthorizationHeader();

    BasicCredentials credentials = authorizationHeaderService.extractBasicCredentialsFrom(authorizationHeader);
    if (credentials == null || credentials.areNotValid()) {
      return AuthenticationResult.FAILED;
    }

    if (wrongCredentialsAreProvided(credentials)) {
      return AuthenticationResult.FAILED;
    }

    return new AuthenticationResult(userService.retrieveUserById(credentials.userId()), true);
  }

  private boolean wrongCredentialsAreProvided(BasicCredentials credentials) {
    try {
      if (userService.verifyUserPassword(credentials.userId(), credentials.password())) {
        return false;
      }
    } catch (Exception exception) {
      exception.printStackTrace();

      throw new UnexpectedFlowException("Failed to verify user credentials.", exception);
    }

    return true;
  }

}
