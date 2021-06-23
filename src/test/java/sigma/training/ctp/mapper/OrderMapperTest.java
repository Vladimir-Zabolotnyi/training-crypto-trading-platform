package sigma.training.ctp.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.training.ctp.dictionary.OrderStatus;
import sigma.training.ctp.dto.OrderDetailsRestDto;
import sigma.training.ctp.persistence.entity.CurrencyEntity;
import sigma.training.ctp.persistence.entity.OrderDetailsEntity;
import sigma.training.ctp.persistence.entity.UserEntity;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {
  private static final Long ID = 1L;
  private static final String SELL_CURRENCY_NAME = "USA_dollar";
  private static final String BUY_CURRENCY_NAME = "Bitcoin";
  private static final BigDecimal SELL_CURRENCY_AMOUNT = new BigDecimal("20");
  private static final BigDecimal BUY_CURRENCY_AMOUNT = new BigDecimal("400");
  private static final CurrencyEntity SELL_CURRENCY = new CurrencyEntity(null ,false,SELL_CURRENCY_NAME,null);
  private static final CurrencyEntity BUY_CURRENCY = new CurrencyEntity(null,false,BUY_CURRENCY_NAME,null);
  private static final OrderStatus ORDER_STATUS = OrderStatus.CREATED;
  private static final UserEntity USER = new UserEntity();
  private static final Instant CREATION_DATE = Instant.ofEpochMilli(1000);

  private static final OrderDetailsEntity ORDER = new OrderDetailsEntity(
    ID, CREATION_DATE,
    USER, ORDER_STATUS,
    SELL_CURRENCY, BUY_CURRENCY,
    SELL_CURRENCY_AMOUNT,BUY_CURRENCY_AMOUNT);

  private static final OrderDetailsRestDto ORDER_DTO = new OrderDetailsRestDto(
    ID, CREATION_DATE,
    USER.getId(), ORDER_STATUS,
    SELL_CURRENCY_NAME,BUY_CURRENCY_NAME,
    SELL_CURRENCY_AMOUNT,BUY_CURRENCY_AMOUNT
   );

  private static final OrderDetailsRestDto ORDER_DTO_WITHOUT_USER = new OrderDetailsRestDto(
    ID, CREATION_DATE,
    null, ORDER_STATUS,
    SELL_CURRENCY_NAME,BUY_CURRENCY_NAME,
    SELL_CURRENCY_AMOUNT,BUY_CURRENCY_AMOUNT);

  OrderMapper orderMapper;

  @BeforeEach
  void setUp() {
    orderMapper = new OrderMapper();
  }

  @Test
  void toRestDto() {
    OrderDetailsRestDto orderDtoActual = orderMapper.toRestDto(ORDER);
    assertEquals(ORDER_DTO, orderDtoActual);
  }

  @Test
  void toEntity() {
    OrderDetailsEntity orderActual = orderMapper.toEntity(ORDER_DTO);
    assertEquals(ORDER, orderActual);
  }

  @Test
  void toEntityWithUser() {
    OrderDetailsEntity orderActual = orderMapper.toEntity(ORDER_DTO_WITHOUT_USER,USER);
    assertEquals(ORDER, orderActual);
  }
}
