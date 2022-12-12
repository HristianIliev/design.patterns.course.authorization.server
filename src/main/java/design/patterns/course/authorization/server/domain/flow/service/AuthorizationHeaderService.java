package design.patterns.course.authorization.server.domain.flow.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthorizationHeaderService {

  public String extractUsernameFrom(String authorizationHeader) {
    BasicCredentials credentials = extractBasicCredentialsFrom(authorizationHeader);
    if (credentials == null) {
      return null;
    }

    return credentials.userId();
  }

  public BasicCredentials extractBasicCredentialsFrom(String authorizationHeader) {
    if (isNotBasicHeader(authorizationHeader)) {
      return null;
    }

    String credentials = base64DecodeCredentials(extractCredentialsFrom(authorizationHeader));

    String[] usernameAndPassword = parse(credentials);
    if (usernameAndPassword == null) {
      return null;
    }

    return new BasicCredentials(usernameAndPassword[0], usernameAndPassword[1]);
  }

  private boolean isNotBasicHeader(String authorizationHeader) {
    return authorizationHeader == null || authorizationHeader.startsWith("Basic ") == false;
  }

  private String extractCredentialsFrom(String authorizationHeader) {
    return authorizationHeader.substring("Basic ".length());
  }

  private String base64DecodeCredentials(String base64EncodedCredentials) {
    try {
      return new String(Base64.getDecoder().decode(base64EncodedCredentials), StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException exception) {
      throw new RuntimeException("Failed to decode basic credentials.", exception);
    }
  }

  private String[] parse(String credentials) {
    int colonIndex = credentials.indexOf(":");
    if (colonIndex == -1) {
      return null;
    }

    String username = credentials.substring(0, colonIndex);
    String password = credentials.substring(colonIndex + 1);

    return new String[] { username, password };
  }

  public String extractBearerFrom(String authorizationHeader) {
    if (isNotBearerHeader(authorizationHeader)) {
      return null;
    }

    return authorizationHeader.substring("Bearer ".length());
  }

  private boolean isNotBearerHeader(String authorizationHeader) {
    return authorizationHeader == null || authorizationHeader.startsWith("Bearer ") == false;
  }

}
