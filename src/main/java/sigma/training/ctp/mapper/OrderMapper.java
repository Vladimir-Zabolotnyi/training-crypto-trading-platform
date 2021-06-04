package sigma.training.ctp.mapper;

import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;

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

  public OrderDetailsEntity toEntity(OrderDetailsRestDto order, UserEntity user) {
    return new OrderDetailsEntity(
      order.getId(),
      order.getCreationDate(),
      user,
      order.getOrderStatus(),
      order.getOrderType(),
      order.getCryptocurrencyPrice(),
      order.getCryptocurrencyAmount());
  }
}