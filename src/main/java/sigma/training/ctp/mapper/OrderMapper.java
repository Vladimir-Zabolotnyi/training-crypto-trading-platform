package sigma.training.ctp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;
import sigma.training.ctp.persistence.repository.OrderDetailsRepository;
import sigma.training.ctp.persistence.repository.UserRepository;
import sigma.training.ctp.service.UserService;

@Component
public class OrderMapper implements Mapper<OrderDetailsEntity, OrderDetailsRestDto> {

  @Override
  public OrderDetailsRestDto toRestDto(OrderDetailsEntity order) {
    return new OrderDetailsRestDto(
      order.getId(),
      order.getCreationDate(),
      order.getUser().getId(),
      order.getOrderStatus(),
      order.getOrderType(),
      order.getCryptocurrencyPrice(),
      order.getCryptocurrencyAmount());

  }

  @Override
  public OrderDetailsEntity toEntity(OrderDetailsRestDto orderDto) {
    UserEntity user = new UserEntity();
    user.setId(orderDto.getUserId());
    return new OrderDetailsEntity(
      orderDto.getId(),
      orderDto.getCreationDate(),
      user,
      orderDto.getOrderStatus(),
      orderDto.getOrderType(),
      orderDto.getCryptocurrencyPrice(),
      orderDto.getCryptocurrencyAmount());
  }
}
