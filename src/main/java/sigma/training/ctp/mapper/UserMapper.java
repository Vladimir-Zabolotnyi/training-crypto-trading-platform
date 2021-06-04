package sigma.training.ctp.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.UserRestDto;
import sigma.training.ctp.persistence.entity.UserEntity;

@Component
public class UserMapper implements Mapper<UserEntity, UserRestDto> {


  @Override
  public UserRestDto toRestDto(UserEntity user) {
   return new UserRestDto(user.getId(), user.getLogin());
  }

  @Override
  public UserEntity toEntity(UserRestDto restDto) {
   UserEntity user = new UserEntity();
   user.setId(restDto.getId());
   user.setLogin(restDto.getLogin());
   return user;
  }
}
