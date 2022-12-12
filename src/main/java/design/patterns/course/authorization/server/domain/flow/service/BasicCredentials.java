package design.patterns.course.authorization.server.domain.flow.service;

public class BasicCredentials {

  private String userId;
  private String password;

  public BasicCredentials(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }

  public String userId() {
    return userId;
  }

  public String password() {
    return password;
  }

  public boolean areNotValid() {
    return userId == null || password == null;
  }

}
