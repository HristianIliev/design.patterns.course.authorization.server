package design.patterns.course.authorization.server.domain.flow.requirements;

import design.patterns.course.authorization.server.domain.entity.user.User;
import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;
import design.patterns.course.authorization.server.domain.flow.exceptions.ForbiddenFlowException;
import design.patterns.course.authorization.server.domain.flow.exceptions.UnauthorizedFlowException;
import design.patterns.course.authorization.server.domain.flow.service.UserIdProvider;
import design.patterns.course.authorization.server.domain.flow.service.factory.ServiceFactory;
import design.patterns.course.authorization.server.domain.service.UserService;

import javax.persistence.EntityNotFoundException;

public class RolesExistence implements FlowRequirement {

  private UserService userService;
  private UserIdProvider userIdProvider;

  public RolesExistence(ServiceFactory serviceFactory) {
    this.userService = serviceFactory.createUserService();
    this.userIdProvider = serviceFactory.createUserIdProvider();
  }

  @Override
  public void fulfilledBy(RequestAttributes attributes) {
    String[] requestRoles = attributes.getRolesAsArray();
    if (requestRoles.length == 0) {
      return;
    }

    String userId = userIdProvider.getUserIdFrom(attributes);

    User user = retrieveUser(userId);
    if (user.containsAllScopes(requestRoles) == false) {
      throw new ForbiddenFlowException("Invalid roles");
    }
  }

  private User retrieveUser(String userId) {
    try {
      return userService.retrieveUserById(userId);
    } catch (EntityNotFoundException exception) {
      throw new UnauthorizedFlowException("Invalid request");
    }
  }

}
