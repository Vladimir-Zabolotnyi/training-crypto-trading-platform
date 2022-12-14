package sigma.training.ctp.mapper;

import org.springframework.stereotype.Component;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.persistence.OrderFilter;

import java.util.Locale;


@Component
public class OrderFilterMapper implements Mapper<OrderFilter, OrderFilterDto> {


  @Override
  public OrderFilterDto toRestDto(OrderFilter orderFilter) {
   return new OrderFilterDto(
      orderFilter.getOrderStatus().toString().toLowerCase(Locale.ROOT),
      orderFilter.getUserId(),orderFilter.getSellCurrencyName(),orderFilter.getBuyCurrencyName());
  }

  @Override
  public OrderFilter toEntity(OrderFilterDto orderFilterDto) {
    OrderFilter orderFilter = new OrderFilter();
    if(orderFilterDto.getOrderStatus()!=null){
      orderFilter.setOrderStatus(OrderStatus.valueOf(orderFilterDto.getOrderStatus().toUpperCase(Locale.ROOT)));
    }
    if(orderFilterDto.getUserId()!=null){
      orderFilter.setUserId(orderFilterDto.getUserId());
    }
    if(orderFilterDto.getSellCurrencyName()!=null){
      orderFilter.setSellCurrencyName(orderFilterDto.getSellCurrencyName());
    }
    if(orderFilterDto.getBuyCurrencyName()!=null){
      orderFilter.setBuyCurrencyName(orderFilterDto.getBuyCurrencyName());
    }
    return orderFilter;
  }
}
