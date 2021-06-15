package sigma.training.ctp.mapper;

import org.springframework.stereotype.Component;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dictionary.OrderType;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.persistence.OrderFilter;

import java.util.Locale;


@Component
public class OrderFilterMapper implements Mapper<OrderFilter, OrderFilterDto> {


  @Override
  public OrderFilterDto toRestDto(OrderFilter orderFilter) {
   return new OrderFilterDto(
      orderFilter.getOrderStatus().toString().toLowerCase(Locale.ROOT),
      orderFilter.getOrderType().toString().toLowerCase(Locale.ROOT),
      orderFilter.getUserId());
  }

  @Override
  public OrderFilter toEntity(OrderFilterDto orderFilterDto) {
    OrderFilter orderFilter = new OrderFilter();
    if(orderFilterDto.getOrderType()!=null){
      orderFilter.setOrderType(OrderType.valueOf(orderFilterDto.getOrderType().toUpperCase(Locale.ROOT)));
    }
    if(orderFilterDto.getOrderStatus()!=null){
      orderFilter.setOrderStatus(OrderStatus.valueOf(orderFilterDto.getOrderStatus().toUpperCase(Locale.ROOT)));
    }
    if(orderFilterDto.getUserId()!=null){
      orderFilter.setUserId(orderFilterDto.getUserId());
    }
    return orderFilter;
  }
}
