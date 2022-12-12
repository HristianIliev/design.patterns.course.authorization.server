package design.patterns.course.authorization.server.domain.flow;

public interface FlowFactory {

  public Flow createFlow(String flowType);

}
