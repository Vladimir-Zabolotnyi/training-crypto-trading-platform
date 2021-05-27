package sigma.training.ctp.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if (Objects.isNull(login) || login.isEmpty()) throw new IllegalArgumentException("login is null");
        final Optional<UserEntity> user = userRepository.findUserByLogin(login);
        if (user.isPresent()) {
            return user.get();
        } else throw new UsernameNotFoundException("User not found");
    }
}
