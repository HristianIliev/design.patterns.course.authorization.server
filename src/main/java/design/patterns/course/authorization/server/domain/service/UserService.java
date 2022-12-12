package design.patterns.course.authorization.server.domain.service;

import design.patterns.course.authorization.server.domain.entity.user.User;

public interface UserService {

  public User retrieveUserById(String userId);

  public boolean verifyUserPassword(String userId, String password);

}
