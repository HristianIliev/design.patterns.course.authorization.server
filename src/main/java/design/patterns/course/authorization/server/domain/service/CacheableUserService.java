package design.patterns.course.authorization.server.domain.service;

import design.patterns.course.authorization.server.domain.entity.user.User;

import java.util.HashMap;
import java.util.Map;

public class CacheableUserService implements UserService {

  private UserService userService;

  private Map<String, User> cache;

  public CacheableUserService(UserService userService) {
    this.userService = userService;

    cache = new HashMap<>();
  }

  @Override
  public User retrieveUserById(String userId) {
    User user = cache.get(userId);
    if (user == null) {
      user = userService.retrieveUserById(userId);

      cache.put(userId, user);
    }

    return user;
  }

  @Override
  public boolean verifyUserPassword(String userId, String password) {
    return userService.verifyUserPassword(userId, password);
  }

}
