package design.patterns.course.authorization.server.domain.entity.user.repository;

import design.patterns.course.authorization.server.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  public Optional<User> findByUsername(String username);

}
