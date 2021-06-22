package sigma.training.ctp.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dto.OrderFilterDto;
import sigma.training.ctp.persistence.OrderFilter;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderFilterMapperTest {
  private static final Long ID = 1L;
  private static final OrderStatus ORDER_STATUS = OrderStatus.CREATED;
  private static final String SELL_CURRENCY_NAME = "USA_dollar";
  private static final String BUY_CURRENCY_NAME = "Bitcoin";
  private static final OrderFilter ORDER_FILTER = new OrderFilter(ORDER_STATUS,ID);
  private static final OrderFilterDto ORDER_FILTER_DTO = new OrderFilterDto(
    ORDER_STATUS.toString().toLowerCase(Locale.ROOT),ID);
  OrderFilterMapper orderFilterMapper;
  @BeforeEach
  void setUp() {
    orderFilterMapper= new OrderFilterMapper();
  }

  @Test
  void toRestDto() {
    OrderFilterDto actual = orderFilterMapper.toRestDto(ORDER_FILTER);
    assertEquals(ORDER_FILTER_DTO,actual);
  }

  @Test
  void toEntity() {
    OrderFilter actual = orderFilterMapper.toEntity(ORDER_FILTER_DTO);
    assertEquals(ORDER_FILTER,actual);
  }
}
