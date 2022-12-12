package design.patterns.course.authorization.server.domain.flow.service;

import design.patterns.course.authorization.server.domain.entity.user.User;

public class AuthenticationResult {

  public static final AuthenticationResult FAILED = new AuthenticationResult(null, false);

  private User user;
  private boolean isAuthenticationSuccessful;

  public AuthenticationResult(User user, boolean isAuthenticationSuccessful) {
    this.user = user;
    this.isAuthenticationSuccessful = isAuthenticationSuccessful;
  }

  public boolean isAuthenticationFailed() {
    return isAuthenticationSuccessful == false;
  }

  public User authenticatedUser() {
    return user;
  }

}
