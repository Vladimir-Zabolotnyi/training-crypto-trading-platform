package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  private static final String SYSTEM_ROOT_LOGIN = ".root";

  @Autowired
  UserRepository userRepository;

  public UserEntity getCurrentUser() {
    return (UserEntity) SecurityContextHolder.getContext()
      .getAuthentication()
      .getPrincipal();
  }

  public UserEntity getRootUser() {
    return userRepository.findUserByLogin(SYSTEM_ROOT_LOGIN)
      .orElseThrow(() -> new UsernameNotFoundException("The root user can not be found"));
  }

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    if (Objects.isNull(login) || login.isEmpty()) {
      throw new IllegalArgumentException("login is null");
    }
    final Optional<UserEntity> user = userRepository.findUserByLogin(login);
    return user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

}
