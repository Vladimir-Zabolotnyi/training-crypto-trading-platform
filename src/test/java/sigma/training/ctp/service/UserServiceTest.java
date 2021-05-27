package sigma.training.ctp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    UserEntity user1;
    UserEntity user2;

    @BeforeEach
    void setUp() {
        user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("Jack");
        user1.setLogin("Jacklog");
        user1.setPassword("Jackpass");

        user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("Vova");
        user2.setLogin("Vovalog");
        user2.setPassword("Vovapass");
    }

    @Test
    void loadUserByUsername() {
        Mockito.when(userRepository.findUserByLogin(user1.getLogin())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findUserByLogin(user2.getLogin())).thenReturn(Optional.of(user2));
        UserDetails userDet1 = userService.loadUserByUsername(user1.getLogin());
        UserDetails userDet2 = userService.loadUserByUsername(user2.getLogin());
        assertEquals(user1, userDet1);
        assertEquals(user2, userDet2);
    }

    @Test()
    void loadUserByNotExistedUsername() {
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user1.getName()));
    }
}