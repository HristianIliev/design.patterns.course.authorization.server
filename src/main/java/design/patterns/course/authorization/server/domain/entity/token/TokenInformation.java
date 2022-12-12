package design.patterns.course.authorization.server.domain.entity.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class TokenInformation {

  private String tokenId;
  @JsonIgnore
  private long createdAt;
  private int expiresInSec;
  private String userId;
  private List<String> roles;

  @JsonIgnore
  public boolean isExpired() {
    Date now = new Date();
    Date expiration = new Date(createdAt + expiresInSec * 1000L);

    return now.after(expiration);
  }

}
