package design.patterns.course.authorization.server.resource;

import design.patterns.course.authorization.server.domain.entity.token.TokenInformation;
import design.patterns.course.authorization.server.domain.flow.AuthorizationServerFlowFactory;
import design.patterns.course.authorization.server.domain.flow.Flow;
import design.patterns.course.authorization.server.domain.flow.FlowFactory;
import design.patterns.course.authorization.server.domain.flow.attributes.RequestAttributes;
import design.patterns.course.authorization.server.domain.flow.exceptions.ForbiddenFlowException;
import design.patterns.course.authorization.server.domain.flow.exceptions.UnexpectedFlowException;
import design.patterns.course.authorization.server.domain.flow.exceptions.UnauthorizedFlowException;
import design.patterns.course.authorization.server.domain.flow.service.AuthorizationHeaderService;
import design.patterns.course.authorization.server.domain.flow.service.factory.ServiceFactory;
import design.patterns.course.authorization.server.domain.service.TokenService;
import design.patterns.course.authorization.server.domain.service.factory.TokenServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/tokens")
public class TokenResource {

  @Autowired
  private ServiceFactory serviceFactory;

  @PostMapping
  public ResponseEntity<TokenInformation> issueToken(HttpServletRequest request) {
    Map<String, String[]> requestParameters = request.getParameterMap();

    RequestAttributes attributes = new RequestAttributes(request, requestParameters);

    FlowFactory flowFactory = new AuthorizationServerFlowFactory(serviceFactory);
    Flow flow = flowFactory.createFlow(attributes.getFlowType());

    flow.validate(attributes);

    flow.process(attributes);

    return ResponseEntity.ok(flow.getResult());
  }

  @GetMapping
  public ResponseEntity<TokenInformation> verifyToken(HttpServletRequest request) {
    Map<String, String[]> requestParameters = request.getParameterMap();

    RequestAttributes attributes = new RequestAttributes(request, requestParameters);

    AuthorizationHeaderService authorizationHeaderService = serviceFactory.createAuthorizationHeaderService();
    String tokenId = authorizationHeaderService.extractBearerFrom(attributes.getAuthorizationHeader());
    if (tokenId == null || tokenId.trim().isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    TokenServiceFactory tokenServiceFactory = serviceFactory.createTokenServiceFactory();
    TokenService tokenService = tokenServiceFactory.createUserCredentialsTokenService(attributes);

    TokenInformation token = tokenService.retrieveTokenById(tokenId);
    if (token.isExpired()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(token);
  }

  @ExceptionHandler({ UnauthorizedFlowException.class })
  public ResponseEntity<String> unsatisfiedFlowExceptionMapper(UnauthorizedFlowException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.error());
  }

  @ExceptionHandler({ UnexpectedFlowException.class })
  public ResponseEntity<String> unexpectedFlowExceptionMapper(UnexpectedFlowException exception) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }

  @ExceptionHandler({ ForbiddenFlowException.class })
  public ResponseEntity<String> forbiddenFlowExceptionMapper(ForbiddenFlowException exception) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.error());
  }

}
