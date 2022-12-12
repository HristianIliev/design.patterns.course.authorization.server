package design.patterns.course.authorization.server.domain.flow.attributes;

import design.patterns.course.authorization.server.domain.flow.service.AuthorizationHeaderService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RequestAttributes {

  private static final String GRANT_TYPE_PARAM = "grant_type";
  private static final String ROLES_PARAM = "roles";

  private HttpServletRequest request;
  private Map<String, String[]> parameters;
  private AuthorizationHeaderService authorizationHeaderService;

  public RequestAttributes(HttpServletRequest request, Map<String, String[]> requestParameters) {
    this.request = request;
    this.parameters = requestParameters;
    this.authorizationHeaderService = new AuthorizationHeaderService();
  }

  public String[] getRolesAsArray() {
    String roles = getRoles();
    if (roles == null) {
      return new String[] {};
    }

    return roles.contains(" ") ? roles.split(" ") : roles.split(",");
  }

  public String getRoles() {
    return getParameter(ROLES_PARAM);
  }

  private String getParameter(String param) {
    String[] list = parameters.get(param);
    if (list == null || list.length == 0) {
      return null;
    }

    return list[0];
  }

  public String getUserIdFromAuthorizationHeader() {
    return authorizationHeaderService.extractUsernameFrom(getAuthorizationHeader());
  }

  public String getAuthorizationHeader() {
    return request.getHeader("Authorization");
  }

  public String getFlowType() {
    return getParameter(GRANT_TYPE_PARAM);
  }

}
