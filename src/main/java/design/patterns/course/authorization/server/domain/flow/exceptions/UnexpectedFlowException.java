package design.patterns.course.authorization.server.domain.flow.exceptions;

public class UnexpectedFlowException extends RuntimeException {

  public UnexpectedFlowException(String message, Throwable throwable) {
    super(message);
  }

}
