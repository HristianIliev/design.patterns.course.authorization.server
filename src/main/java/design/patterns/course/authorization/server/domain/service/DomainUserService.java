package design.patterns.course.authorization.server.domain.service;

import design.patterns.course.authorization.server.domain.entity.user.User;
import design.patterns.course.authorization.server.domain.entity.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.persistence.EntityNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Optional;

@Service
public class DomainUserService implements UserService {

  private UserRepository userRepository;

  @Autowired
  public DomainUserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User retrieveUserById(String userId) {
    Optional<User> user = userRepository.findByUsername(userId);
    if (user.isEmpty()) {
      throw new EntityNotFoundException("User with given username does not exist.");
    }

    return user.get();
  }

  @Override
  public boolean verifyUserPassword(String userId, String password) {
    Optional<User> optionalUser = userRepository.findByUsername(userId);
    if (optionalUser.isEmpty()) {
      return false;
    }

    User user = optionalUser.get();

    String encryptedPassword;
    try {
      encryptedPassword = encryptPassword(password, user.getSalt());
    } catch (Exception exception) {
      exception.printStackTrace();

      return false;
    }

    return encryptedPassword.equals(user.getPassword());
  }

  private String encryptPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    String algorithm = "PBKDF2WithHmacSHA1";
    int derivedKeyLength = 160;
    int iterations = 20000;

    byte[] saltBytes = Base64.getDecoder().decode(salt);
    KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
    SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);

    byte[] encodedBytes = factory.generateSecret(spec).getEncoded();
    return Base64.getEncoder().encodeToString(encodedBytes);
  }

}
