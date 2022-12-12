package design.patterns.course.authorization.server.domain.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(schema = "identity_provider")
public class User {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Setter(AccessLevel.NONE)
  @JsonIgnore
  private Long id;
  private String email;
  private String username;
  @JsonIgnore
  private String password;
  @JsonIgnore
  private String salt;
  private String description;
  private LocalDateTime createdAt;
  @Lob
  private byte[] picture;
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<Role> roles;

  public boolean containsAllScopes(String[] requestedRoles) {
    if (roles == null) {
      return false;
    }

    for (String requestedRole : requestedRoles) {
      if (roleIsAssigned(requestedRole)) {
        continue;
      }

      return false;
    }

    return true;
  }

  private boolean roleIsAssigned(String requestedRole) {
    for (Role role : roles) {
      if (requestedRole.equals(role.getName())) {
        return true;
      }
    }

    return false;
  }

  public String getRolesAsString() {
    return roles.stream()
        .map(Role::getName)
        .collect(Collectors.joining(","));
  }

}
