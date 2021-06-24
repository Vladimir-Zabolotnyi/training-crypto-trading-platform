package sigma.training.ctp.mapper;

import org.springframework.stereotype.Component;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper implements Mapper<OrderDetailsEntity, OrderDetailsRestDto> {

  @Override
  public OrderDetailsRestDto toRestDto(OrderDetailsEntity order) {
    return new OrderDetailsRestDto(
      order.getId(),
      order.getCreationDate(),
      order.getUser().getId(),
      order.getOrderStatus(),
      order.getSellCurrency().getName(),
      order.getBuyCurrency().getName(),
      order.getSellCurrencyAmount(),
      order.getBuyCurrencyAmount());
  }

  public List<OrderDetailsRestDto> toRestDto(List<OrderDetailsEntity> orderList) {
    return orderList.stream().map(
      this::toRestDto).collect(Collectors.toList());

  }

  @Override
  public OrderDetailsEntity toEntity(OrderDetailsRestDto orderDto) {
    UserEntity user = new UserEntity();
    user.setId(orderDto.getUserId());
    CurrencyEntity currencyToSell = new CurrencyEntity();
    CurrencyEntity currencyToBuy = new CurrencyEntity();
    currencyToSell.setName(orderDto.getSellCurrencyName());
    currencyToBuy.setName(orderDto.getBuyCurrencyName());
    return new OrderDetailsEntity(
      orderDto.getId(),
      orderDto.getCreationDate(),
      user,
      orderDto.getOrderStatus(),
      currencyToSell,
      currencyToBuy,
      orderDto.getSellCurrencyAmount(),
      orderDto.getBuyCurrencyAmount());
  }

  public OrderDetailsEntity toEntity(OrderDetailsRestDto orderDto, UserEntity user) {
    CurrencyEntity currencyToSell = new CurrencyEntity();
    CurrencyEntity currencyToBuy = new CurrencyEntity();
    currencyToSell.setName(orderDto.getSellCurrencyName());
    currencyToBuy.setName(orderDto.getBuyCurrencyName());
    return new OrderDetailsEntity(
      orderDto.getId(),
      orderDto.getCreationDate(),
      user,
      orderDto.getOrderStatus(),
      currencyToSell,
      currencyToBuy,
      orderDto.getSellCurrencyAmount(),
      orderDto.getBuyCurrencyAmount());
  }
}
