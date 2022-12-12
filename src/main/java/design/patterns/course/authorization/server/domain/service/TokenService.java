package design.patterns.course.authorization.server.domain.service;

import design.patterns.course.authorization.server.domain.entity.token.TokenInformation;
import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;

public interface TokenService {

  public TokenInformation issueToken(RequestAttributes attributes);

  public TokenInformation retrieveTokenById(String tokenId);

}
