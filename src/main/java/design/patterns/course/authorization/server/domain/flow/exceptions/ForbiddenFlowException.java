package design.patterns.course.authorization.server.domain.flow.exceptions;

public class ForbiddenFlowException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String error;

  public ForbiddenFlowException(String error) {
    this.error = error;
  }

  public String error() {
    return String.valueOf(error);
  }

}
