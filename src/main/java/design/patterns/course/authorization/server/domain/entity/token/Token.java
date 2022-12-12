package design.patterns.course.authorization.server.domain.entity.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(schema = "authorization_server")
public class Token {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Setter(AccessLevel.NONE)
  @JsonIgnore
  private Long id;

  private String tokenId;
  private long createdAt;
  private int expiresInSec;
  private Long userId;
  private String roles;

}
