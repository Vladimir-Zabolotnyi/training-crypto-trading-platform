package sigma.training.ctp.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sigma.training.ctp.dto.UserRestDto;
import sigma.training.ctp.persistence.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
  private static final UserEntity USER = new UserEntity();
  private static final UserRestDto USER_DTO = new UserRestDto(1l, "login");

  UserMapper userMapper;

  @BeforeEach
  void setUp() {
    userMapper = new UserMapper();
    USER.setId(1L);
    USER.setLogin("login");
  }


  @Test
  void toRestDto() {
    assertEquals(USER_DTO, userMapper.toRestDto(USER));
  }

  @Test
  void toEntity() {
    assertEquals(USER, userMapper.toEntity(USER_DTO));

  }
}
